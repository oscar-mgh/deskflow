FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app
COPY . .
RUN ./mvnw install -DskipTests

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java", "-XX:+UseSerialGC", "-Xss512k", "-XX:MaxRAMPercentage=75.0", "-jar", "/app.jar"]