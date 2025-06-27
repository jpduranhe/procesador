# Prueba Técnica
The following was discovered as part of building this project:


# Guía de Instalación y Ejecución del Proyecto

## 📋 Requisitos Previos

- Docker instalado en su sistema operativo
- Acceso a Internet para la descarga de imágenes
- Permisos suficientes para ejecutar contenedores Docker
- Revisar las varialbles de entorno en el archivo `docker-compose.yml`

```yaml
    environment:
      # Variables de entorno para Spring Boot
      - FILE_PROCESSING_CHUNK_SIZE=100
      - FILE_PROCESSING_NAME=lote.simple.json

    volumes:
        - ./processing-app:/output # Ruta de salida para los archivos XML
        - ./processing-app:/input # Ruta de entrada para el archivo JSON
```

## 🚀 Pasos para la Ejecución

### 1. Ejecutar el Comando de Docker Compose

```bash
docker compose up -d
```

### 3. Ejecutar el Contenedor
dentro de la carpeta processing-app en la base  se crearan las carpetas outHtml que tendrá el html del informe salida, la carpeta aoutXML que tenra los archivos XML generados


## 🔧 Especificaciones Técnicas

- **Base**: Imagen Java Azul Zulu OpenJDK 21
- **Construcción**: Maven 3.9.6 con JDK 21
- **Artefacto**: Aplicación Spring Boot empaquetada como JAR

## ⚠️ Consideraciones Importantes

- Los tests se omiten durante la fase de construcción de la imagen
- La configuración de la aplicación se realiza mediante el archivo JAR ejecutable

## 📝 Verificación de Funcionamiento

Para verificar que la aplicación está funcionando correctamente, acceda a:

```
http://localhost:8080
```

## 📞 Soporte

En caso de problemas durante la instalación o ejecución, contacte al equipo de desarrollo.