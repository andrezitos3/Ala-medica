# ===== Builder: Maven + Java 17 =====
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copia arquivos mínimos primeiro
COPY BackEnd/pom.xml BackEnd/mvnw ./BackEnd/

RUN chmod +x BackEnd/mvnw || true

# Baixa dependências sem cache
RUN cd BackEnd && ./mvnw -B -DskipTests dependency:go-offline

# Copia o código e faz o build
COPY BackEnd/src ./BackEnd/src
RUN cd BackEnd && ./mvnw -B -DskipTests package

# ===== Runtime: Distroless Java 17 =====
FROM gcr.io/distroless/java17-debian12:nonroot AS runtime

EXPOSE 8080

WORKDIR /app

COPY --from=build /workspace/BackEnd/target/*-SNAPSHOT.jar /app/app.jar

USER 65532

ENTRYPOINT ["java","-jar","/app/app.jar"]
