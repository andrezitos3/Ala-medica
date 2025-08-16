# Etapa 1: build
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia pom.xml e baixa dependências para cache
COPY BackEnd/pom.xml ./BackEnd/
RUN mvn -f ./BackEnd/pom.xml dependency:go-offline

# Copia o código fonte e compila
COPY BackEnd ./BackEnd
RUN mvn -f ./BackEnd/pom.xml clean package -DskipTests

# Etapa 2: runtime
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copia apenas o JAR do estágio anterior
COPY --from=builder /app/BackEnd/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
