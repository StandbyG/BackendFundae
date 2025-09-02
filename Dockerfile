# ========== Etapa 1: Construcción (BUILDER) =====================
# Usamos una imagen de Maven para construir la aplicación.
FROM maven:3.8.5-openjdk-17 AS builder

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia los archivos de Maven para descargar las dependencias
COPY pom.xml .

# Descarga las dependencias para evitar reconstruirlas en cada cambio de código
RUN mvn dependency:go-offline

# Copia el código fuente
COPY src ./src

# Construye la aplicación y crea el archivo JAR final
RUN mvn clean package -DskipTests

# ========== Etapa 2: Ejecución (RUNNER) =========================
# Usa una imagen de Eclipse Temurin para la aplicación final.
FROM eclipse-temurin:17-jre-alpine

# Copia el archivo JAR desde la etapa de "builder"
COPY --from=builder /app/target/*.jar /app/app.jar

# Establece el directorio de trabajo
WORKDIR /app

# Expone el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor se inicie
CMD ["java", "-jar", "app.jar"]