FROM eclipse-temurin:17-jdk as builder
WORKDIR /app
COPY . .

# Даем права на выполнение mvnw
RUN chmod +x mvnw

# Теперь можно запускать
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]