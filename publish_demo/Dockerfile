FROM openjdk:11 as build
WORKDIR /workdir
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install

FROM openjdk:11
COPY --from=build /workdir/target/*.jar /app/publisher.jar
ENTRYPOINT ["java","-jar","/app/publisher.jar"]