FROM eclipse-temurin:25-jre

# Carpeta de trabajo
WORKDIR /app

# Copiar directamente el JAR ya construido por Maven/Eclipse
COPY target/*.jar /app/app.jar

# Exponer el puerto configurado en application.yml
EXPOSE 8081

# Comando de arranque
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]