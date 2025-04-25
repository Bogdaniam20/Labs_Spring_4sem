FROM eclipse-temurin:17-jdk as builder
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "while ! nc -z $DB_HOST 5432; do sleep 2; done && java -Dserver.port=${PORT:-8080} -jar app.jar"]