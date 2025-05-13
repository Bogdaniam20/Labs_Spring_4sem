FROM maven:3.8.6-eclipse-temurin-17 as builder
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B package -DskipTests -Dcheckstyle.skip=true
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
ENV JAVA_OPTS="-XX:+UseSerialGC -Xss256k -XX:MaxRAM=256m -XX:MaxRAMPercentage=70.0"
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]