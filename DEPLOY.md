
Система на удаленном сервере Ubuntu 22.04.5 LTS

1. Выполнить
```bash
sudo apt update && sudo apt upgrade -y
```

2. Создать директорию
```bash
mkdir -p /docker/target
```

3. Переместить из проекта на удаленный сервер в `/docker/target`
- trex-budget-bot-0.0.1-SNAPSHOT.jar

4. Переместить из проекта на удаленный сервер в `/docker` 
- .env
- bot-docker-compose.yml
- Dockerfile

5. На удаленном сервере отредактировать Dockerfile
```dockerfile
# Build stage
#FROM maven:3.9.6-eclipse-temurin-21 AS build
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

6. Запуск
```bash
docker-compose -f bot-docker-compose.yml up -d
```

7. Выводить логи
```bash
docker-compose -f bot-docker-compose.yml logs -f
```

8. Остановка
```bash
docker-compose -f bot-docker-compose.yml down
```