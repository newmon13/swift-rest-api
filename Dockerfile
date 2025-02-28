FROM maven:3.9-eclipse-temurin-19 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:19-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV JAVA_OPTS=""
ENV SPRING_DATA_MONGODB_HOST=mongo
ENV SPRING_DATA_MONGODB_PORT=27017
ENV SPRING_DATA_MONGODB_DATABASE=myapp
ENV SPRING_DATA_MONGODB_USERNAME=root
ENV SPRING_DATA_MONGODB_PASSWORD=123zxcvbnm

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]