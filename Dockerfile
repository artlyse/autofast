# Etapa 1: Compilar la aplicación
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar pom.xml y descargar dependencias (para cachear)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final (solo el JAR)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Ejecutar la aplicación con el perfil "render"
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=render", "app.jar"]
