FROM openjdk:17
VOLUME /tmp
EXPOSE 80
ADD ./target/games-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]