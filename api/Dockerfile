FROM openjdk:11 as build
WORKDIR /workdir
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install

FROM openjdk:11
ARG HOSTNAME
ENV server.name=$HOSTNAME
COPY --from=build /workdir/target/*.jar /app/api.jar
ENTRYPOINT ["java","-jar","/app/api.jar"]