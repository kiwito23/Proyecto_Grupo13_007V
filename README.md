# 🛍️ Tienda Ropa - Arquitectura de Microservicios


## 📋 Descripción
Sistema de tienda de ropa desarrollado con arquitectura de microservicios usando Spring Boot, MySQL, Docker y JWT.


## 👥 Integrantes (Grupo 13)
- Silvia Valenzuela 
- Katerine Olmedo 


## 📦 Microservicios

| Microservicio | Puerto | Responsable | Descripción                                |
-------------------------------------------------------------------------------------
| ms-categorias | 8082   | Silvia      | Gestión de categorías de ropa              |
| ms-tallas     | 8089   | Silvia      | Gestión de tallas                          |
| ms-resenas    | 8088   | Silvia      | Gestión de reseñas de productos            |
| ms-envios     | 8083   | Silvia      | Gestión de envíos                          |
| ms-pagos      | 8085   | Silvia      | Gestión de pagos                           |
| ms-carrito    | 8081   | Katerine    | Gestión del carrito de compras             |
| ms-inventario | 8084   | Katerine    | Gestión de inventario                      | 
| ms-pedidos    | 8086   | Katerine    | Gestión de pedidos                         |
| ms-productos  | 8087   | Katerine    | Gestión de productos                       | 
| ms-usuarios   | 8090   | Katerine    | Gestión de usuarios                        |
| api-gateway   | 8080   | Silvia      | Gateway principal                          |
| eureka-server | 8761   | Silvia      | Servidor de descubrimiento de servicios    |


## 🌐 Rutas del API Gateway

| Ruta                          | Microservicio         |
---------------------------------------------------------
| `/api/carrito/**              | ms-carrito:8081       |
| `/api/categorias/**`          | ms-categorias:8082    |
| `/api/envios/**`              | ms-envios:8083        |
| `/api/inventario/**           | ms-inventario:8084    |
| `/api/pagos/**`               | ms-pagos:8085         |
| `/api/pedidos/**              | ms-pedidos:8086       |
| `/api/productos/**            | ms-productos:8087     |
| `/api/resenas/**`             | ms-resenas:8088       |
| `/api/tallas/**`              | ms-tallas:8089        |
| `/api/usuarios/**, /auth/**   | ms-usuarios:8090      |


## 🔍 Eureka Server — Service Discovery
Panel de registro de microservicios disponible en: http://localhost:8761

Todos los microservicios se registran automáticamente al iniciar.


## 📚 Documentación Swagger

| Microservicio | URL Swagger                                     |
-------------------------------------------------------------------
| ms-categorias | http://localhost:8082/doc/swagger-ui/index.html |
| ms-tallas     | http://localhost:8089/doc/swagger-ui/index.html |
| ms-resenas    | http://localhost:8088/doc/swagger-ui/index.html |
| ms-envios     | http://localhost:8083/doc/swagger-ui/index.html |
| ms-pagos      | http://localhost:8085/doc/swagger-ui/index.html |


## 🔐 Autenticación
Todos los endpoints (excepto `/auth/login` y GET de productos) requieren token JWT.

**Obtener token:**

POST http://localhost:8090/auth/login
Content-Type: application/json

{
    "email": "admin68@TiendaRopa.cl",
    "contrasena": "SilvKat68"
}

Usar el token retornado como "Bearer Token" en el header "Authorization".

--------------------------------------------------------------------------------------

## --- 🚀 Instrucciones de Ejecución --- ##

# Requisitos
- Docker Desktop
- Java 17
- Maven

# Ejecución local con Docker
```bash
cd Proyecto_Grupo13_007V
docker-compose up --build
```

# Verificar que todos los servicios estén corriendo
```bash
docker ps
```
Deben aparecer 22 contenedores: 
10 microservicios + 10 bases de datos MySQL + api-gateway + eureka-server


# Ejecución de pruebas unitarias
```bash
cd ms-categorias/ms-categorias
.\mvnw test

### Base de datos para pruebas
Las pruebas unitarias usan **H2** (base de datos en memoria) configurada en `application-test.properties`, separada de MySQL de producción.

| Entorno           | Base de datos | Configuración               |
|-------------------|---------------|-----------------------------|
|Docker/Producción  | MySQL 8.0     | application.yml             |
| Pruebas unitarias | H2 en memoria | application-test.properties |

-----------------------------------------------------------------------------------

## 🛠️ Stack Tecnológico
- **Backend:** Spring Boot 3.2.5 / 4.0.6
- **Base de datos:** MySQL 8.0 
- **Contenedores:** Docker + Docker Compose
- **Seguridad:** Spring Security + JWT (JJWT)
- **Migraciones:** Flyway
- **Comunicación:** WebClient 
- **API Gateway:** Spring Cloud Gateway
- **Documentación:** SpringDoc OpenAPI / Swagger UI
- **Testing:** JUnit 5 + Mockito
- **Build:** Maven