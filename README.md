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

### Usuarios
- Un usuario puede ser **empleado del depósito** o **contacto de una empresa cliente**, pero nunca ambos al mismo tiempo.
- Los roles `SUPER_ADMIN` y `OPERADOR` no están vinculados a ninguna empresa.

### Stock
- El stock se divide en `disponible` y `reservado`.
- Al crear un pedido, el stock de cada ítem se **reserva** automáticamente.
- Al confirmar el pedido, el stock reservado se **consume**.
- Al devolver un pedido, el stock se **libera** o **devuelve** según el estado anterior.
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
| Spring Data JPA | — |
| MapStruct | 1.5.5 |
| Lombok | — |
| MySQL | 8+ |
| OpenAPI / Swagger | springdoc-openapi |
| JavaMailSender | Spring Mail |

> **Seguridad JWT**: pendiente de implementación. La arquitectura está diseñada para filtrar datos por `empresa_id` extraído del token.

---

## Requisitos previos

- Java 21
- MySQL 8+
- Maven (o usar el wrapper `./mvnw` incluido)

---

## Configuración de base de datos

Crear la base de datos en MySQL:

```sql
CREATE DATABASE fulfilltrack_db;
```

Configurar las credenciales en `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fulfilltrack_db
    username: root
    password: root
```

El esquema se genera automáticamente al levantar la aplicación (`ddl-auto: update`).

---

## Ejecución

```bash
# Compilar
./mvnw clean package

# Ejecutar
./mvnw spring-boot:run

# Ejecutar sin tests
./mvnw spring-boot:run -DskipTests
```

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
├── common/
│   └── exception/          # Excepciones globales + handler (@RestControllerAdvice)
│   └──utils/               # Clase de utilidades generales  
└── features/
    └── <modulo>/           # Una carpeta por entidad de dominio
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
  "username": "KevinLomonaco",
  "password": "admin123"