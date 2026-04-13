FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY gradlew gradlew
COPY gradlew.bat gradlew.bat
COPY gradle gradle
COPY settings.gradle.kts build.gradle.kts ./
COPY src src

RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends wget \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
