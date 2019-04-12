FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY SpringBootRedis-0.0.1-SNAPSHOT.jar springboot.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","/springboot.jar"]
