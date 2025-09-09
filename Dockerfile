FROM maven:3.8.6-amazoncorretto-17
WORKDIR /app
COPY . .
EXPOSE 8081
RUN mvn install --no-transfer-progress -DskipTests=true
ENTRYPOINT ["mvn", "spring-boot:run"]
