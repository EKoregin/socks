FROM maven:3.6.3-openjdk-17-slim
RUN mkdir socks
WORKDIR socks
COPY . .
RUN mvn package -Dmaven.test.skip=true
EXPOSE 8881
CMD ["java", "-jar", "target/socks-0.0.1.jar"]