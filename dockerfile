#1st stage: building

FROM openjdk:21 AS build

COPY . /build

WORKDIR /build

RUN chmod +x mvnw

RUN ./mvnw package -DskipTests

#2nd stage: running

FROM azul/zulu-openjdk-alpine:21-jre-latest

WORKDIR /app

COPY --from=build /build/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app/app.jar"]