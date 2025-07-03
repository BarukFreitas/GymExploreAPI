# Estágio 1: Build da aplicação
FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY src/main/resources/postgres_config .
# Compila o projeto e empacota o JAR executável
RUN mvn clean package -DskipTests

# Estágio 2: Criação da imagem final
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copia o JAR do estágio de build
COPY --from=build /app/target/gym-explore-backend-0.0.1-SNAPSHOT.jar app.jar
# Expõe a porta que sua aplicação Spring Boot usa (8080 por padrão)
EXPOSE 8080
# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]