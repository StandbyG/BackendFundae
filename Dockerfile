# Usa una imagen base de Java (por ejemplo, OpenJDK 17)
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos de construcción de Maven
COPY pom.xml .
COPY src ./src

# Construye el proyecto y crea el JAR
RUN ./mvnw clean package

# Expone el puerto que tu aplicación usa (por defecto 8080 en Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor se inicie
ENTRYPOINT ["java", "-jar", "target/*.jar"]