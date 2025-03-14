# 使用 JDK 8 运行时环境
FROM anolis-registry.cn-zhangjiakou.cr.aliyuncs.com/openanolis/openjdk:8-8.6

# 设置工作目录
WORKDIR /app

# 复制本地编译好的 JAR 文件到容器
COPY ruoyi-admin/target/*.jar app.jar

# 运行 Java 应用
CMD ["java", "-jar", "app.jar"]  
