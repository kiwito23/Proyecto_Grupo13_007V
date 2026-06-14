# 🛍️ Tienda Ropa - Arquitectura de Microservicios

## 📋 Descripción
Sistema de tienda de ropa desarrollado con arquitectura de microservicios usando Spring Boot, MySQL, Docker y JWT.

## 👥 Integrantes
- Silvia (Grupo 13)
- Katerine (Grupo 13)

## 📦 Microservicios

| Microservicio | Puerto | Responsable | Descripción |
|---|---|---|---|
| ms-categorias | 8082 | Silvia | Gestión de categorías de ropa |
| ms-tallas | 8089 | Silvia | Gestión de tallas |
| ms-resenas | 8088 | Silvia | Gestión de reseñas de productos |
| ms-envios | 8083 | Silvia | Gestión de envíos |
| ms-pagos | 8085 | Silvia | Gestión de pagos |
| ms-carrito | 8081 | Katerine | Gestión del carrito de compras |
| ms-inventario | 8084 | Katerine | Gestión de inventario |
| ms-pedidos | 8086 | Katerine | Gestión de pedidos |
| ms-productos | 8087 | Katerine | Gestión de productos |
| ms-usuarios | 8090 | Katerine | Gestión de usuarios |
| api-gateway | 8080 | Silvia | Gateway principal |

## 🌐 Rutas del API Gateway

| Ruta | Microservicio |
|---|---|
| `/api/categorias/**` | ms-categorias:8082 |
| `/api/tallas/**` | ms-tallas:8089 |
| `/api/resenas/**` | ms-resenas:8088 |
| `/api/envios/**` | ms-envios:8083 |
| `/api/pagos/**` | ms-pagos:8085 |

## 📚 Documentación Swagger

| Microservicio | URL Swagger |
|---|---|
| ms-categorias | http://localhost:8082/doc/swagger-ui/index.html |
| ms-tallas | http://localhost:8089/doc/swagger-ui/index.html |
| ms-resenas | http://localhost:8088/doc/swagger-ui/index.html |
| ms-envios | http://localhost:8083/doc/swagger-ui/index.html |
| ms-pagos | http://localhost:8085/doc/swagger-ui/index.html |

## 🚀 Instrucciones de Ejecución

### Requisitos
- Docker Desktop
- Java 17
- Maven

### Ejecución local con Docker
```bash
cd Proyecto_Grupo13_007V
docker-compose up --build
```

### Ejecución de pruebas unitarias
```bash
cd ms-categorias/ms-categorias
.\mvnw test
```

## 🛠️ Stack Tecnológico
- **Backend:** Spring Boot 3.2.5
- **Base de datos:** MySQL 8.0
- **Contenedores:** Docker
- **Seguridad:** JWT
- **Migraciones:** Flyway
- **Documentación:** Swagger/OpenAPI
- **Testing:** JUnit 5 + Mockito