# MemberFlow-API

## Descripción del Proyecto

**MemberFlow-API** es el módulo de interfaz de usuario y controladores de la aplicación **MemberFlow**, diseñado para gestionar membresías, usuarios, clases y finanzas en un entorno organizacional. Este proyecto actúa como la capa de presentación y comunicación, exponiendo servicios RESTful que interactúan con el módulo de datos **MemberFlow-Data**.

El objetivo principal de **MemberFlow-API** es proporcionar una interfaz segura y eficiente para que los clientes (como aplicaciones web o móviles) puedan interactuar con la lógica de negocio y la base de datos subyacente.

---

## Características Principales

- **Controladores REST**: Exposición de endpoints para gestionar usuarios, roles, notificaciones, clases y finanzas.
- **Seguridad**: Implementación de autenticación basada en JWT (JSON Web Tokens).
- **Integración con MemberFlow-Data**: Uso del módulo de datos para acceder a la lógica de negocio y la persistencia.
- **Configuración Centralizada**: Uso de `application.properties` para gestionar parámetros clave como JWT y otros ajustes.
- **Ejecución con Spring Boot**: Aplicación basada en el framework Spring Boot para facilitar el desarrollo y despliegue.
- **Modularidad**: Integración con el módulo **MemberFlow-Data** para mantener una separación clara entre la lógica de negocio y la capa de presentación.

---

## Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes y archivos principales:

### 1. **Configuración**
- **`application.properties`**: Archivo de configuración que incluye parámetros clave como JWT y ajustes de la aplicación.
- **`MemberFlowApplication`**: Clase principal para iniciar la aplicación Spring Boot.

### 2. **Controladores**
- **`AdminController`**: Gestiona las operaciones relacionadas con los administradores, como la creación y gestión de usuarios administrativos.
- **`AuthController`**: Maneja la autenticación y generación de tokens JWT para garantizar la seguridad de los endpoints.
- **`StudentController`**: Proporciona endpoints para gestionar estudiantes, incluyendo inscripción, actualización de datos y consultas.
- **`TeacherController`**: Gestiona las operaciones relacionadas con los profesores, como la asignación de grupos y la gestión de clases.
- **`UserController`**: Controlador genérico para operaciones relacionadas con usuarios, como la gestión de perfiles y roles.

### 3. **Seguridad**
- **`SecurityConfig`**: Configuración de seguridad para proteger los endpoints mediante autenticación JWT.
- **`JwtAuthFilter`**: Filtro para validar los tokens JWT en cada solicitud, asegurando que solo usuarios autenticados puedan acceder a los recursos protegidos.
- **`JwtUtil`**: Utilidad para generar y validar tokens JWT, incluyendo la configuración de tiempos de expiración y claves secretas.

### 4. **Recursos**
- **`application.properties`**: Archivo de configuración centralizado para parámetros clave de la aplicación.

---

## Configuración del Proyecto

### Requisitos Previos

1. **Java 17 o superior**
2. **Maven**
3. **Dependencia del módulo MemberFlow-Data**

### Ejecución del Proyecto

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/memberflow-api.git
   cd memberflow-api