FROM openjdk:8u252-slim
COPY <app-dir-path-on-host> /app/
WORKDIR /app/
CMD ["java", "-jar", "${service-name}.jar","--deploymentConfig","dep.yml"]