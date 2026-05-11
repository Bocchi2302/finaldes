# Sistema de Gestion de Inventario y Ventas

Aplicacion web desarrollada con Spring Boot para administrar productos, registrar ventas y controlar inventario con autenticacion JWT y base de datos PostgreSQL.

## Resumen del sistema

El sistema esta enfocado en dos roles:

- `ADMIN`: puede crear, actualizar, consultar y desactivar productos, ademas de registrar y consultar ventas.
- `EMPLEADO`: puede consultar inventario y registrar ventas.

Flujo principal:

`Frontend` -> `Controller` -> `Service` -> `Repository` -> `PostgreSQL`

## Tecnologias

- Java 17
- Spring Boot 4
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- Thymeleaf
- PostgreSQL
- Maven

## Estructura general

```text
com.example.parcial2
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
│   ├── custom
│   └── handler
├── mapper
├── repository
├── security
├── service
└── service.impl
```

## Modulos del sistema

### Autenticacion
- Registro de usuario.
- Inicio de sesion con JWT.
- Respuesta con token y rol.

### Inventario
- Alta de productos.
- Edicion de productos.
- Consulta de productos.
- Desactivacion logica de productos.
- Control de stock minimo y estado del producto.

### Ventas
- Registro de ventas.
- Descuento automatico de stock.
- Consulta de ultimas ventas.

## Roles y permisos

- `ADMIN`
  - Puede crear, editar, consultar y desactivar productos.
  - Puede registrar y consultar ventas.
- `EMPLEADO`
  - Puede consultar productos e inventario.
  - Puede registrar y consultar ventas.

## Credenciales demo

Al iniciar la aplicacion se crean estos usuarios de prueba:

- `admin@inventario.com` / `Admin123*`
- `empleado@inventario.com` / `Empleado123*`

## Base de datos

El proyecto usa PostgreSQL. La configuracion actual esta en [src/main/resources/application.properties](src/main/resources/application.properties).

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/parcial2db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Crear la base

```sql
CREATE DATABASE parcial2db;
```

## Como ejecutar

1. Asegurate de tener PostgreSQL corriendo.
2. Crea la base `parcial2db`.
3. Verifica usuario y clave en `application.properties`.
4. Ejecuta la aplicacion:

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

5. Abre el front desde:

```text
http://localhost:8080/app/login
```

## Frontend integrado

El front esta servido desde `src/main/resources/templates` y `src/main/resources/static`.

- `templates` contiene las vistas Thymeleaf.
- `static/css` contiene los estilos.
- `static/js` contiene la logica que consume la API con `fetch`.

Paginas disponibles:

- `/` -> landing
- `/app/login` -> login
- `/app/dashboard` -> panel principal
- `/app/productos` -> gestion de productos
- `/app/inventario` -> inventario
- `/app/ventas` -> ventas

## Endpoints de autenticacion

- `POST /auth/register`
- `POST /auth/login`

Ejemplo de login:

```json
{
  "email": "admin@inventario.com",
  "password": "Admin123*"
}
```

Respuesta:

```json
{
  "status": "success",
  "code": 200,
  "message": "Inicio de sesión exitoso",
  "data": {
    "token": "jwt-token",
    "expiresIn": 86400000,
    "role": "ROLE_ADMIN",
    "user": {
      "id": 1,
      "fullName": "Admin Inventario",
      "email": "admin@inventario.com",
      "role": "ROLE_ADMIN"
    }
  }
}
```

## Endpoints de productos

- `POST /productos`
- `GET /productos`
- `GET /productos/{id}`
- `PUT /productos/{id}`
- `DELETE /productos/{id}`
- `GET /inventario`

Ejemplo de request:

```json
{
  "nombre": "Cuaderno universitario",
  "categoria": "Papeleria",
  "precioUnitario": 3500,
  "stock": 40,
  "stockMinimo": 10,
  "activo": true,
  "fechaVencimiento": null
}
```

## Endpoints de ventas

- `POST /ventas`
- `GET /ventas`

Ejemplo de request:

```json
{
  "productoId": 1,
  "cantidad": 2,
  "fechaVenta": "2026-05-10"
}
```

## Seguridad

- La API es stateless.
- El token JWT se envia en `Authorization: Bearer <token>`.
- Las rutas publicas son las de autenticacion y las paginas del front.
- Las operaciones de escritura estan protegidas por rol.

## Manejo de errores

Las excepciones del negocio se responden con un formato consistente:

```json
{
  "status": "error",
  "code": 409,
  "message": "Stock insuficiente para el producto X",
  "errors": ["Stock insuficiente para el producto X"],
  "timestamp": "2026-05-10T21:40:00Z",
  "path": "/ventas"
}
```

## Colecciones de Postman

En la carpeta `postman/` se incluyen colecciones para probar la API:

- `parcial2-auth.collection.json` -> autenticacion.
- `parcial2-crm.collection.json` -> debe renombrarse o reutilizarse para inventario/ventas.

Recomendacion: si vas a seguir con esta version del proyecto, cambia la segunda coleccion a algo como `parcial2-inventario-ventas.collection.json` para que el nombre quede coherente.

## Diagrama ER

```mermaid
erDiagram
  USER {
    BIGINT id PK
    STRING full_name
    STRING email UK
    STRING password
    ENUM role
  }

  PRODUCTO {
    BIGINT id PK
    STRING nombre UK
    STRING categoria
    DECIMAL precio_unitario
    INT stock
    INT stock_minimo
    BOOLEAN activo
    DATE fecha_vencimiento
    DATETIME creado_en
    DATETIME actualizado_en
  }

  VENTA {
    BIGINT id PK
    BIGINT producto_id FK
    INT cantidad
    DECIMAL precio_unitario
    DECIMAL total
    DATE fecha_venta
    DATETIME registrado_en
    BIGINT registrado_por_usuario_id FK
  }

  USER ||--o{ VENTA : registra
  PRODUCTO ||--o{ VENTA : genera
```

## Notas de implementacion

- El backend usa DTOs para entrada y salida.
- La logica de negocio vive en los servicios.
- La conversion entre DTO y entidad esta separada en mappers.
- El front consume la API desde JavaScript con `fetch`.

## Siguiente mejora sugerida

Si quieres seguir afinando el proyecto, el siguiente paso util es renombrar la coleccion `parcial2-crm.collection.json` para que coincida con el dominio actual y agregar ejemplos reales de headers JWT en Postman.
