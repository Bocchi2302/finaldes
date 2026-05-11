# Refactor CRM - B1 a B5

## B1. Modelo de datos y entidades JPA
Se agregaron las entidades `Cliente`, `Contacto` y `Oportunidad`.

- Cliente:
- PK: `id`
- Atributos principales: `nombre`, `correo`, `telefono`, `direccion`
- Relación con Usuario: `@ManyToOne usuarioAsignado`
- Relación con Contacto: `@OneToMany(mappedBy = "cliente")`
- Relación con Oportunidad: `@OneToMany(mappedBy = "cliente")`

- Contacto:
- PK: `id`
- Atributos: `nombre`, `correo`, `telefono`, `cargo`
- Relación: `@ManyToOne cliente`

- Oportunidad:
- PK: `id`
- Atributos: `titulo`, `descripcion`, `monto`, `etapa`, `fechaCierreEstimada`, `creadoEn`
- Relación con Cliente: `@ManyToOne cliente`
- Relación con Usuario: `@ManyToOne creadoPor`

### ¿Cuándo usar `mappedBy`?
`mappedBy` se usa en el lado inverso de la relación bidireccional para indicar que la clave foránea la administra la entidad hija (`Contacto` u `Oportunidad`).
Esto evita tablas intermedias innecesarias y define un solo dueño de la relación.

## B2. Repositorios y servicios
### Repositorios (JpaRepository)
- `ClienteRepository`
- `ContactoRepository`
- `OportunidadRepository`

### Servicios
- `ClienteCommandService`: crear, actualizar, asociar contacto, eliminar.
- `ClienteQueryService`: listar y consultar por id.
- `OportunidadService`: crear oportunidad para un cliente.
- `ClientePermissionService`: regla de permisos para eliminar cliente (solo ADMIN).

### SRP en servicios
Se separaron responsabilidades por tipo de operación:
- Lectura (query) separada de escritura (command).
- Reglas de autorización aisladas en `ClientePermissionService`.
- Lógica de oportunidad en su propio servicio.

## B3. API REST y DTOs
| Método | Ruta | DTO request | DTO response | HTTP esperado |
|---|---|---|---|---|
| POST | `/clientes` | `ClienteRequest` | `ClienteResponse` | 201 |
| GET | `/clientes/{id}` | - | `ClienteResponse` | 200 |
| GET | `/clientes` | - | `List<ClienteResponse>` | 200 |
| PUT | `/clientes/{id}` | `ClienteRequest` | `ClienteResponse` | 200 |
| DELETE | `/clientes/{id}` | - | `Void` | 200 |
| POST | `/clientes/{id}/oportunidades` | `OportunidadRequest` | `OportunidadResponse` | 201 |
| POST | `/clientes/{id}/contactos` | `ContactoRequest` | `ContactoResponse` | 201 |

## B4. Validación y manejo de excepciones
Validaciones agregadas en DTOs:
- `@NotBlank`
- `@NotNull`
- `@Email`
- `@Size`
- `@DecimalMin`

Manejo de errores con `@RestControllerAdvice`:
- Cliente no existe -> `ResourceNotFoundException` (404)
- Correo inválido -> `MethodArgumentNotValidException` (400)
- Usuario sin permiso -> `UnauthorizedAccessException` o `AccessDeniedException` (403)

## B5. Seguridad Spring Security 6 + JWT
Estrategia aplicada:
- Autenticación JWT con API stateless (`SessionCreationPolicy.STATELESS`).
- Roles definidos: `ADMIN`, `VENDEDOR`, `LECTOR` (internamente `ROLE_*`).
- Autorización por endpoint en `SecurityConfig`.
- Restricción obligatoria cumplida: solo `ADMIN` puede eliminar clientes.
- Regla reforzada también en servicio (`ClientePermissionService`).
