# Stage 1: Build
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /home/gradle/project

# Copia tudo do contexto para dentro da imagem
COPY . .

# Dá permissão no gradlew
RUN chmod +x ./gradlew

# Roda o build completo, gerando o jar do módulo 'app' (runner)
RUN ./gradlew :app:bootJar --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copia o jar gerado da etapa de build
COPY --from=build /home/gradle/project/app/build/libs/*.jar app.jar

# Exponha a porta da aplicação (ajuste se precisar)
EXPOSE 8080

# Comando para rodar o jar
ENTRYPOINT ["java", "-jar", "app.jar"]
