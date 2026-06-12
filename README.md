# FulfillTrack

Sistema de **Fulfillment Multitenant** desarrollado con Spring Boot para la gestión logística de múltiples empresas clientes.

Permite centralizar la recepción de pedidos, control de stock, preparación, despacho y liquidación de múltiples empresas desde una sola plataforma, con aislamiento de datos por empresa.

---

## Problemática que resuelve

Los servicios de fulfillment operan simultáneamente con múltiples empresas cliente, lo que genera desafíos como:

- Separación de datos entre clientes en una infraestructura compartida
- Control de stock en tiempo real con reservas por pedido
- Seguimiento del ciclo de vida completo de cada pedido
- Trazabilidad de todos los movimientos de stock
- Cálculo y notificación automática de costos operativos por empresa

FulfillTrack aborda estos problemas mediante una arquitectura multitenant con procesos automatizados de validación, trazabilidad y liquidación.

---

## Reglas de negocio

### Usuarios y roles
- Un usuario puede ser **empleado del depósito** o **contacto de una empresa cliente**, pero nunca ambos al mismo tiempo.
- Los roles disponibles son: `OPERADOR`, `ADMIN`, `EMPRESA` y `PENDIENTE`.
- Los roles `OPERADOR` y `ADMIN` no están vinculados a ninguna empresa.

### Stock
- El stock se divide en `disponible` y `reservado`.
- Al crear un pedido, el stock de cada ítem se **reserva** automáticamente.
- Al confirmar el pedido, el stock reservado se **consume**.
- Al devolver un pedido, el stock se **libera** según el estado anterior.
- No se puede desactivar un producto con stock disponible o reservado mayor a cero.
- Cada operación de stock genera un **movimiento de auditoría** con tipo y motivo.

### Pedidos — máquina de estados
```
RECIBIDO → CONFIRMADO → EN_PREPARACION → LISTO_PARA_DESPACHO → DESPACHADO
                                                                     ↑
                                                              (cualquier estado) → DEVUELTO
```
No se permiten transiciones fuera de este flujo.

### Despachos
- Un pedido solo puede tener un despacho (relación 1 a 1).
- Solo se puede despachar un pedido en estado `LISTO_PARA_DESPACHO`.

### Liquidaciones
- Se calculan como `total_despachos × precio_despacho`.
- Se generan **automáticamente el 1° de cada mes a las 8:00 AM** para todas las empresas activas.
- Al generar la liquidación se envía una notificación por email a la empresa.
- Las liquidaciones impagas del mes anterior generan un recordatorio de pago en el mismo proceso.

---

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 4.x |
| Spring Security + JWT | — |
| Spring Data JPA | — |
| MapStruct | 1.5.5 |
| Lombok | — |
| MySQL | 8+ |
| OpenAPI / Swagger | springdoc-openapi 3.x |
| JavaMailSender | Spring Mail |

---

## Ejecución con Docker (recomendado)

Requiere únicamente tener **Docker Desktop** instalado y corriendo. No es necesario tener Java ni MySQL instalados localmente.

### 1. Configurar variables de entorno

```bash
cp .env.example .env
```

Editar `.env` con los valores reales:

```env
# Base de datos
DB_USERNAME=root
DB_PASSWORD=root

# JWT
JWT_SECRET=cambia_esto_por_un_secreto_seguro_de_al_menos_32_chars
JWT_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=86400000

# Mail (Gmail)
MAIL_USERNAME=tu_correo@gmail.com
MAIL_PASSWORD=tu_app_password
MAIL_FROM=tu_correo@gmail.com
```

### 2. Levantar el proyecto

```bash
docker compose up --build
```

Docker levantará automáticamente la base de datos MySQL y la aplicación. El esquema se crea solo al iniciar.

La API queda disponible en `http://localhost:8080`.

---

## Ejecución local (desarrollo)

Requiere Java 21, MySQL 8+ y Maven (o el wrapper `./mvnw` incluido).

### 1. Crear la base de datos

```sql
CREATE DATABASE fulfilltrack_db;
```

### 2. Configurar variables de entorno

Definir las siguientes variables en el entorno o en el IDE antes de ejecutar:

```
DB_USERNAME=root
DB_PASSWORD=root
JWT_SECRET=...
JWT_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=86400000
MAIL_USERNAME=...
MAIL_PASSWORD=...
MAIL_FROM=...
```

### 3. Ejecutar

```bash
# Compilar y ejecutar
./mvnw spring-boot:run

# Ejecutar sin correr tests
./mvnw spring-boot:run -DskipTests
```

---

## Autenticación

Todos los endpoints (excepto `/api/auth/**`) requieren un token JWT en el header:

```
Authorization: Bearer <token>
```

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/auth/registrarse` | Crear nueva cuenta de usuario |
| `POST` | `/api/auth/ingresar` | Login — devuelve access token y refresh token |
| `POST` | `/api/auth/refresh` | Renovar access token con el refresh token |

---

## Documentación de la API

Con la aplicación corriendo, acceder a Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

Todos los endpoints están documentados con descripción, parámetros y códigos de respuesta posibles.

---

## Estructura del proyecto

```
com.fulfilltrack.FulfillTrack
├── auth/                       # Seguridad JWT, filtros, roles y autenticación
├── common/
│   ├── exception/              # Excepciones globales + handler (@RestControllerAdvice)
│   └── utils/                  # Clase de utilidades generales
└── features/
    └── <modulo>/               # Una carpeta por entidad de dominio
        ├── Entity
        ├── Repository
        ├── IService / Service
        ├── Controller
        ├── dto/
        └── mapper/
```

---

## Notas

- No incluye integración real con couriers ni pasarelas de pago (alcance académico).
- El scheduler de liquidaciones requiere configuración de servidor SMTP para el envío de emails.