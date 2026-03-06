# 多阶段构建 - 构建阶段
FROM docker.m.daocloud.io/library/maven:3.8.4-amazoncorretto-8 AS builder

# 设置工作目录
WORKDIR /build

# 复制pom文件
COPY pom.xml .
COPY smzs-mall-common/pom.xml ./smzs-mall-common/
COPY smzs-mall-app/pom.xml ./smzs-mall-app/

# 下载依赖
RUN mvn dependency:go-offline -B

# 复制源代码
COPY smzs-mall-common/src ./smzs-mall-common/src
COPY smzs-mall-app/src ./smzs-mall-app/src

# 编译项目
RUN mvn clean package -DskipTests -pl smzs-mall-app -am

# 运行阶段
FROM docker.m.daocloud.io/amazoncorretto:8-alpine

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建应用目录
WORKDIR /app

# 从构建阶段复制jar包
COPY --from=builder /build/smzs-mall-app/target/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]