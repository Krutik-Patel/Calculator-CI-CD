FROM openjdk:17
COPY ./target/calculator_app-1.0-SNAPSHOT-jar-with-dependencies.jar ./
WORKDIR ./
CMD [“java”,”-cp”,” calculator_app-1.0-SNAPSHOT-jar-with-dependencies.jar”,”com.calculator.Calculator”]