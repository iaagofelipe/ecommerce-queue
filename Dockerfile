# Estágio 1: Build - Compila o projeto
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# --- OTIMIZAÇÃO DE CACHE ---
# 1. Copia APENAS o pom.xml
COPY pom.xml .

# 2. Baixa as dependências. Esta camada só será refeita se o pom.xml mudar.
RUN mvn dependency:go-offline

# 3. Agora sim, copia o resto do código-fonte.
# Se só o código mudar, os passos acima serão reutilizados do cache.
COPY src ./src
# --- FIM DA OTIMIZAÇÃO ---

# Compila o projeto e gera o .jar
RUN mvn package -DskipTests

# Estágio 2: Run - Imagem final, leve e otimizada
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]