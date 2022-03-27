FROM openjdk:8
VOLUME /tmp
EXPOSE 8080
#RUN echo $(ls -la /usr/local/src )
COPY ./target/SpringBootMicroServices-0.0.1-SNAPSHOT.jar app/SpringBootMicroServices-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dfile.encoding=UTF-8", "-Dspring.profiles.active=dev", "-jar", "-Xms4G", "-Xmx10G", "app/SpringBootMicroServices-0.0.1-SNAPSHOT.jar"]