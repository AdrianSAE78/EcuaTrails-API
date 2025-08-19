# Etapa de construcción
FROM eclipse-temurin:17-jdk-alpine as builder

# Instalar Maven
RUN apk add --no-cache maven

WORKDIR /app

# Copiar archivo de configuración de Maven
COPY pom.xml .

# Descargar dependencias (esto se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src src

# Compilar aplicación
RUN mvn package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar solo el JAR compilado desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
