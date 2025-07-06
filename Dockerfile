FROM amazoncorretto:22

LABEL version="1.0"

EXPOSE 8080:8080

WORKDIR /app

COPY build/libs/travel-thirdparty-touroperator-service-adapter-0.0.1-SNAPSHOT.jar travel-thirdparty-touroperator-service.jar

ENTRYPOINT ["java", "-jar", "travel-thirdparty-touroperator-service"]
