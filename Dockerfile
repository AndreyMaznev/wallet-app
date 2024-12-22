FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY . /app/.
#RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=false

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY /out/artifacts/*.jar /app/*.jar
EXPOSE 8181
#ADD /src/main/resources/application.properties /app/application.properties
#ADD src/main/resources/application.yml /app/application.yml
ENTRYPOINT ["java", "-jar", "/app/*.jar"]