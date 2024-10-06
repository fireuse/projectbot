FROM openjdk:17
#RUN gradle jar
COPY build/libs/bot-1.0-SNAPSHOT.jar /app/bot.jar

ENTRYPOINT ["java", "-jar", "/app/bot.jar"]