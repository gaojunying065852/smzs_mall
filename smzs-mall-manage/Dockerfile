# 单阶段运行镜像 - 使用预编译的JAR文件
FROM docker.m.daocloud.io/amazoncorretto:8-alpine

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建应用目录
WORKDIR /app

# 复制预编译的JAR文件（需要在构建时提供）
COPY target/*.jar app.jar

# 暴露端口
EXPOSE 8081

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]