FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

# FIX PERMISSION
RUN chmod +x mvnw

# (optional debug – helps confirm)
RUN ls -l mvnw

RUN ./mvnw clean package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/invoicegeneratorapi-0.0.1-SNAPSHOT.jar"]