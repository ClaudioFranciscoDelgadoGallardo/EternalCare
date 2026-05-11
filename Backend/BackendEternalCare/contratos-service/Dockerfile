FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/*.jar app.jar

# Cambiar este puerto según el microservicio: 8080 (BFF), 8081 (Inventario), 8082 (Programación), 8083 (Contratos)
EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
