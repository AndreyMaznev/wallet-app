FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY . /app/.
#RUN mvn dependency:go-offline
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

#FROM eclipse-temurin:17-jre-alpine
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar
#COPY /out/artifacts/*.jar /app/*.jar
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "/app/*.jar"]