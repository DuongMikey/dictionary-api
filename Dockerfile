
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
# Cấp quyền thực thi cho mvnw
RUN chmod +x mvnw
# Tải dependencies trước để cache layer
RUN ./mvnw dependency:go-offline -B
# Copy source code và đóng gói
COPY src src
RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Port mặc định thường dùng trên cloud
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]