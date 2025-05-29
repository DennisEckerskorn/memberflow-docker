# MemberFlow API

MemberFlow es una plataforma integral para la gestión de academias de artes marciales, con funcionalidades que cubren usuarios, clases, asistencias, membresías, pagos y facturación. Este repositorio corresponde al **backend API** desarrollado en **Java + Spring Boot**.

---

## 🧱 Arquitectura General

El proyecto sigue una estructura modular con separación clara por dominios:

- **user_management_controllers**: gestión de usuarios (Admins, Teachers, Students, Roles, Permisos).
- **class_management_controllers**: gestión de clases, grupos, asistencias y membresías.
- **finance_management**: facturación, productos, líneas de factura, pagos, IVA.
- **config**: configuración de seguridad, Swagger y Web CORS.

---

## 🔐 Seguridad y JWT

Se utiliza Spring Security con JWT para autenticar y autorizar usuarios. El token se genera mediante el `AuthController` y se debe incluir en las peticiones siguientes vía header:

```
Authorization: Bearer <token>
```

### Roles soportados:

- `ADMIN`: acceso completo.
- `TEACHER`: acceso a su información y la de sus estudiantes.
- `STUDENT`: acceso solo a su perfil.

---

## 📂 Endpoints REST por módulo

### 🔑 Autenticación

| Método | URL                  | Descripción            |
|--------|----------------------|------------------------|
| POST   | `/auth/login`        | Iniciar sesión         |
| GET    | `/users/me`          | Datos del usuario logeado |

---

### 👥 Gestión de Usuarios

- `/api/v1/admins`
- `/api/v1/students`
- `/api/v1/teachers`
- `/api/v1/roles`
- `/api/v1/permissions`
- `/api/v1/notifications`

Soporta operaciones: `create`, `update`, `getById`, `getAll`, `delete`.

---

### 🏋️‍♂️ Gestión de Clases

| Entidad        | Endpoint Base                 |
|----------------|-------------------------------|
| TrainingGroup  | `/api/v1/training-groups`     |
| TrainingSession| `/api/v1/training-sessions`   |
| Assistance     | `/api/v1/assistances`         |
| Membership     | `/api/v1/memberships`         |

Las sesiones pueden generarse automáticamente por grupo. Las asistencias se vinculan a estudiantes y sesiones.

---

### 💰 Finanzas

| Entidad         | Endpoint Base               |
|-----------------|-----------------------------|
| Invoice         | `/api/v1/invoices`          |
| InvoiceLine     | `/api/v1/invoice-lines`     |
| ProductService  | `/api/v1/products`          |
| Payment         | `/api/v1/payments`          |
| IVAType         | `/api/v1/iva-types`         |

Incluye generación de PDF de facturas vía:

```
GET /api/v1/invoices/{id}/pdf
```

---

## 📄 Swagger UI

Disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

Configurado a través de `SwaggerConfig.java`. Expone todos los endpoints REST con documentación interactiva.

---

## ⚙️ Configuración

Archivo: `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mf_db
spring.datasource.username=root
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=validate
spring.profiles.active=dev (opcional para datos de prueba)
```

---

## 🧪 Datos de prueba

La clase `TestDataSeeder` inserta automáticamente entidades para facilitar pruebas: usuarios, roles, permisos, clases, facturas, etc. Se activa con el perfil `dev`.

---

## 🧾 Uso y construcción

### En desarrollo local:

```bash
mvn clean install
```

Luego ejecutar `MemberFlowApplication` desde tu IDE.

### Docker:

Integrado en `docker-compose`, se despliega junto a MySQL y frontend automáticamente.

---

## ✅ Pruebas

```bash
mvn test
```

Incluye pruebas unitarias para cada servicio (`UserService`, `InvoiceService`, `MembershipService`, etc.).

---

## 📦 DTOs y Validaciones

Cada entidad tiene su correspondiente DTO con validaciones `javax.validation`, mapeo bidireccional y lógica encapsulada. Esto asegura una capa REST limpia y desacoplada del modelo de base de datos.

---