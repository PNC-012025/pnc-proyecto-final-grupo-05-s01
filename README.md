# Mercaduca — Sistema Web de Gestión Emprendedora UCA

## Descripción general

**Mercaduca** es el primer local emprendedor dentro del campus de la **Universidad Centroamericana (UCA)**.  
Este sistema web busca digitalizar y optimizar la gestión del local, facilitando la inscripción, seguimiento y visibilidad de los emprendimientos estudiantiles.

---

## Deployment

- **Backend (Spring Boot):** https://mercaduca-c132791fdef9.herokuapp.com/swagger-ui/index.html#/

---

## Diagramas Entidad - Relación (Base de datos)

![diagramabd n-capas](https://github.com/user-attachments/assets/6fe330c2-4d44-4a91-b9ef-0b39f7074781)

![business](https://github.com/user-attachments/assets/2858c5ef-1978-4a58-8703-f5e06297fc92)

---

## Tecnologías utilizadas

| Capa         | Tecnología           |
|--------------|----------------------|
| Backend      | Spring Boot          |
| Base de datos| PostgreSQL (DigitalOcean)          |
| Deploy       | Heroku               |
---

## Instrucciones de compilación

> **Recomendación:** se sugiere utilizar **IntelliJ IDEA** como entorno de desarrollo para facilitar la ejecución del proyecto.

### 1. Clonar el repositorio
### 2. Abrir el proyecto en IntelliJ IDEA:

Selecciona la carpeta que contiene el archivo al abrir IntelliJ.

Esperar a que se descarguen las dependencias automáticamente o hacer clic en el botón “Build” → “Build Project”

### 3. Ejecutar el proyecto:

Una vez finalizada la descarga de dependencias y el build del proyecto, presionar el botón verde de **"Run"** que aparece en la parte superior de IntelliJ.

El backend se levantará por defecto en `http://localhost:8080`.
