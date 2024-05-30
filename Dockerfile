FROM bellsoft/liberica-openjdk-alpine:17
ADD target/book-lender-jpa-0.0.1-SNAPSHOT.jar book-lender-docker.jar
ENTRYPOINT ["java", "-jar", "/book-lender-docker.jar"]