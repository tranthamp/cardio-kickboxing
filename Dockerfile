FROM openjdk:8-alpine

COPY target/uberjar/cardio-kickboxing.jar /cardio-kickboxing/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/cardio-kickboxing/app.jar"]
