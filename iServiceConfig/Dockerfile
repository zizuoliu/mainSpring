FROM openjdk:11

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} serviceconfig.jar

ENTRYPOINT ["java", "-jar", "/serviceconfig.jar"]

EXPOSE 8051