# ===== Stage 1: Build =====
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml riêng để cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source và build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ===== Stage 2: Runtime =====
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Tạo user không có quyền root để tăng bảo mật
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy JAR từ stage build
COPY --from=build /app/target/datn_backend_spring-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]