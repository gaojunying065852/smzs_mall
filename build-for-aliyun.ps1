# Local build script for Aliyun Docker build (PowerShell version)

Write-Host "Starting local compilation..." -ForegroundColor Green

# 1. Clean and compile the entire project
Write-Host "Compiling project..." -ForegroundColor Yellow
mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "Maven compilation failed" -ForegroundColor Red
    exit 1
}

# 2. Verify compilation results
Write-Host "Verifying compilation results..." -ForegroundColor Yellow

$appJar = Get-ChildItem -Path "smzs-mall-app/target" -Filter "*.jar" -ErrorAction SilentlyContinue
$manageJar = Get-ChildItem -Path "smzs-mall-manage/target" -Filter "*.jar" -ErrorAction SilentlyContinue
$commonJar = Get-ChildItem -Path "smzs-mall-common/target" -Filter "*.jar" -ErrorAction SilentlyContinue

if (-not $appJar) {
    Write-Host "smzs-mall-app compilation artifact missing" -ForegroundColor Red
    exit 1
}

if (-not $manageJar) {
    Write-Host "smzs-mall-manage compilation artifact missing" -ForegroundColor Red
    exit 1
}

if (-not $commonJar) {
    Write-Host "smzs-mall-common compilation artifact missing" -ForegroundColor Red
    exit 1
}

Write-Host "Compilation verification passed" -ForegroundColor Green

# 3. Create directory structure for Aliyun build
Write-Host "Preparing Aliyun build package..." -ForegroundColor Yellow

# Create build directory
$buildDir = "aliyun-build"
if (Test-Path $buildDir) {
    Remove-Item -Recurse -Force $buildDir
}
New-Item -ItemType Directory -Path $buildDir -Force | Out-Null

# Copy Dockerfiles and compiled artifacts for each module
Write-Host "Copying app module files..." -ForegroundColor Yellow
New-Item -ItemType Directory -Path "$buildDir/smzs-mall-app" -Force | Out-Null
New-Item -ItemType Directory -Path "$buildDir/smzs-mall-app/target" -Force | Out-Null
Copy-Item "smzs-mall-app/Dockerfile" "$buildDir/smzs-mall-app/"
Copy-Item "smzs-mall-app/target/*.jar" "$buildDir/smzs-mall-app/target/"

Write-Host "Copying manage module files..." -ForegroundColor Yellow
New-Item -ItemType Directory -Path "$buildDir/smzs-mall-manage" -Force | Out-Null
New-Item -ItemType Directory -Path "$buildDir/smzs-mall-manage/target" -Force | Out-Null
Copy-Item "smzs-mall-manage/Dockerfile" "$buildDir/smzs-mall-manage/"
Copy-Item "smzs-mall-manage/target/*.jar" "$buildDir/smzs-mall-manage/target/"

# Copy common configuration files
Write-Host "Copying common configuration files..." -ForegroundColor Yellow
Copy-Item ".dockerignore" "$buildDir/"
Copy-Item "pom.xml" "$buildDir/"

# Create Aliyun build script
$buildScript = @'
#!/bin/bash

# Aliyun Docker image build script

# Set variables
REGISTRY="registry.cn-hangzhou.aliyuncs.com"  # Modify to your Aliyun image registry address
NAMESPACE="your-namespace"                     # Modify to your namespace
VERSION=$(date +%Y%m%d-%H%M%S)

echo "Starting Docker image build..."

# Build app service image
echo "Building smzs-mall-app image..."
cd smzs-mall-app
docker build -t ${REGISTRY}/${NAMESPACE}/smzs-mall-app:${VERSION} .
docker push ${REGISTRY}/${NAMESPACE}/smzs-mall-app:${VERSION}
cd ..

# Build manage service image
echo "Building smzs-mall-manage image..."
cd smzs-mall-manage
docker build -t ${REGISTRY}/${NAMESPACE}/smzs-mall-manage:${VERSION} .
docker push ${REGISTRY}/${NAMESPACE}/smzs-mall-manage:${VERSION}
cd ..

echo "Image build completed!"
echo "Image addresses:"
echo "App service: ${REGISTRY}/${NAMESPACE}/smzs-mall-app:${VERSION}"
echo "Manage service: ${REGISTRY}/${NAMESPACE}/smzs-mall-manage:${VERSION}"

# Optional: Tag as latest version
echo "Tagging latest version..."
docker tag ${REGISTRY}/${NAMESPACE}/smzs-mall-app:${VERSION} ${REGISTRY}/${NAMESPACE}/smzs-mall-app:latest
docker tag ${REGISTRY}/${NAMESPACE}/smzs-mall-manage:${VERSION} ${REGISTRY}/${NAMESPACE}/smzs-mall-manage:latest
docker push ${REGISTRY}/${NAMESPACE}/smzs-mall-app:latest
docker push ${REGISTRY}/${NAMESPACE}/smzs-mall-manage:latest

echo "All operations completed!"
'@

$buildScript | Out-File -FilePath "$buildDir/build-images.sh" -Encoding UTF8

# 4. Package upload files
Write-Host "Creating upload package..." -ForegroundColor Yellow
$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$packageName = "smzs-mall-aliyun-build-$timestamp.tar.gz"

# Use tar command to package (requires system support)
try {
    tar -czf $packageName $buildDir
    Write-Host "Build package preparation completed!" -ForegroundColor Green
} catch {
    Write-Host "tar command execution failed, please manually compress the $buildDir directory" -ForegroundColor Yellow
    Write-Host "Or use 7-Zip or other tools to compress this directory" -ForegroundColor Yellow
    $packageName = $buildDir
}

Write-Host ""
Write-Host "Build information:" -ForegroundColor Cyan
Write-Host "  Package name: $packageName"
Write-Host "  Contents:"
Write-Host "    - smzs-mall-app/ (app service)"
Write-Host "    - smzs-mall-manage/ (manage service)"
Write-Host "    - build-images.sh (Aliyun build script)"
Write-Host "    - configuration files"
Write-Host ""
Write-Host "Usage steps:" -ForegroundColor Cyan
Write-Host "1. Upload $packageName to Aliyun ECS server"
Write-Host "2. Extract: tar -xzf $packageName"
Write-Host "3. Enter directory: cd aliyun-build"
Write-Host "4. Modify REGISTRY and NAMESPACE in build-images.sh"
Write-Host "5. Execute build: ./build-images.sh"
Write-Host ""
Write-Host "Tips:" -ForegroundColor Yellow
Write-Host "- Ensure Docker is installed and running on Aliyun server"
Write-Host "- Ensure logged into Aliyun ACR: docker login registry.cn-hangzhou.aliyuncs.com"
Write-Host "- Modify image registry address and namespace according to your ACR configuration"
Write-Host "- Check docs/阿里云ACR构建配置说明.md for detailed ACR setup guide"