FROM openjdk:17
COPY ./target/calculator_app-1.0-SNAPSHOT-jar-with-dependencies.jar ./
WORKDIR ./
CMD ["java","-jar","calculator_app-1.0-SNAPSHOT-jar-with-dependencies.jar"]