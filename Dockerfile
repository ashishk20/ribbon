FROM openjdk:8
ADD target/ribbon.jar ribbon.jar
ENTRYPOINT ["java", "-jar","ribbon.jar"]