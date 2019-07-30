@echo off
title 商盟助手电商平台部署脚本

echo 🚀 开始部署商盟助手电商平台...

REM 构建项目
echo 📦 正在构建项目...
call mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo ❌ 项目构建失败
    pause
    exit /b 1
)

echo ✅ 项目构建成功

REM 启动Docker容器
echo 🐳 正在启动Docker容器...
docker-compose up -d

if %errorlevel% neq 0 (
    echo ❌ Docker容器启动失败
    pause
    exit /b 1
)

echo ✅ Docker容器启动成功

REM 等待服务启动
echo ⏳ 等待服务启动...
timeout /t 30 /nobreak >nul

REM 检查服务状态
echo 🔍 检查服务状态...

REM 检查小程序服务
curl -f http://localhost:8080/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ 小程序服务启动成功 (http://localhost:8080)
) else (
    echo ⚠️  小程序服务可能启动失败
)

REM 检查管理后台服务
curl -f http://localhost:8081/actuator/health >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ 管理后台服务启动成功 (http://localhost:8081)
) else (
    echo ⚠️  管理后台服务可能启动失败
)

REM 检查Nginx
curl -f http://localhost/health >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Nginx反向代理启动成功 (http://localhost)
) else (
    echo ⚠️  Nginx可能启动失败
)

echo 🎉 部署完成！
echo.
echo 📊 服务访问地址：
echo   小程序接口: http://localhost:8080
echo   管理后台接口: http://localhost:8081
echo   统一入口: http://localhost
echo.
echo 🔧 管理命令：
echo   查看日志: docker-compose logs -f
echo   停止服务: docker-compose down
echo   重启服务: docker-compose restart
echo.
pause