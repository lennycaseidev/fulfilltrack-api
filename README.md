#  FulfillTrack

Sistema de **Fulfillment Multitenant** desarrollado con Spring Boot para la gestión logística de múltiples empresas clientes.

El sistema permite centralizar la recepción de mercadería, almacenamiento, gestión de pedidos y despacho, garantizando aislamiento de datos por empresa mediante autenticación JWT.

---

##  Problemática que resuelve

Los servicios de fulfillment trabajan con múltiples empresas al mismo tiempo, lo que genera desafíos como:

* Separación segura de datos entre clientes
* Control de stock en tiempo real
* Procesamiento eficiente de pedidos masivos
* Seguimiento del estado de pedidos
* Cálculo de costos operativos por empresa

FulfillTrack aborda estos problemas mediante una arquitectura multitenant y procesos automatizados de validación y trazabilidad.

---

##  Reglas de negocio principales

* Cada usuario pertenece a una **empresa** (excepto SUPER_ADMIN y OPERADOR)

* Los datos se filtran por `empresa_id` obtenido desde el JWT

* El **stock** se divide en:

  * disponible
  * reservado

* Los pedidos siguen una **máquina de estados**:

RECIBIDO → CONFIRMADO → EN_PREPARACION → LISTO_PARA_DESPACHO → DESPACHADO → ENTREGADO
↘ DEVUELTO

* No se permite avanzar a estados inválidos

* El stock se:

  * **reserva** al importar pedidos
  * **descuenta** al confirmar

* Cada movimiento de stock genera trazabilidad

* Las liquidaciones se calculan como:

  `total_despachos × precio_despacho`

---

##  Procesos críticos

* Importación masiva de pedidos con validación de stock
* Control de transición de estados de pedidos
* Generación de despachos con tracking
* Registro de movimientos de stock
* Generación de liquidaciones por empresa

---

##  Tecnologías

* Java + Spring Boot
* Spring Data JPA
* MySQL
* Spring Security + JWT
* OpenAPI (Swagger)

---

##  Autenticación

La API utiliza JWT.
Cada request debe incluir:

Authorization: Bearer {token}

---

##  Ejecución del proyecto

1. Clonar el repositorio

2. Configurar la base de datos en `application.properties`

3. Ejecutar la aplicación desde el IDE o con Maven

4. Acceder a la documentación Swagger:

http://localhost:8080/swagger-ui.html

---

##  Notas

* Arquitectura multitenant basada en `empresa_id`
* Los operadores no pertenecen a una empresa
* No incluye integraciones reales con couriers ni pagos (alcance académico)
