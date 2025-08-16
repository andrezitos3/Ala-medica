FROM maven:3.9-eclipse-temurin-17

WORKDIR /app

# Copia todo o projeto (pasta alamedica) para dentro do container
COPY alamedica ./alamedica

# Compila e empacota o app usando o pom.xml dentro de alamedica
RUN mvn -f ./alamedica/pom.xml clean package

EXPOSE 8080

# Roda o jar automaticamente, sem precisar saber o nome
CMD java -jar $(find alamedica/target -name "*.jar" | head -n 1)
