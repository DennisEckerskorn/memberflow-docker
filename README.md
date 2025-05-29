# 🚀 MemberFlow - Entorno Dockerizado

Este repositorio contiene la configuración necesaria para levantar **MemberFlow**, una aplicación de gestión para escuelas de artes marciales, usando contenedores Docker. Incluye servicios para base de datos, backend en Spring Boot y frontend en React.

---

## 📁 Estructura del Proyecto

```
memberflow-docker/
│
├── docker/
│   ├── docker-compose.yml            # Orquestador de servicios
│   ├── mysql-data/                   # Volumen persistente de MySQL (no se sube al repo)
│   ├── Dockerfile-api                # Dockerfile para el backend
│   ├── Dockerfile-front              # Dockerfile para el frontend
│   ├── nginx.conf                    # Configuración para Nginx
│   ├── initdb.sh (opcional)         # Script para restaurar base desde backup.sql
│   └── backup.sql (opcional)        # Backup opcional de base de datos
│
├── memberflow-api/                   # Backend en Spring Boot (API)
├── memberflow-data/                  # Módulo con entidades, servicios y repositorios
├── memberflow-frontend/              # Frontend en React
├── Documentación/                    # Archivos de documentación
└── .gitignore                        # Excluye mysql-data del control de versiones
```

---

## 🧠 Sobre la persistencia de datos

- Los datos de MySQL se guardan en la carpeta `docker/mysql-data/` gracias al volumen configurado en Docker.
- Esta carpeta **no se sube a Git** (está en `.gitignore`), por lo tanto si clonas el repositorio en otro equipo, deberás crearla vacía o regenerarla al iniciar la aplicación.

---

## ⚙️ Configuración inicial del backend

Archivo: `memberflow-api/src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://mysql:3306/mf_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=validate
# Para desarrollo inicial (activar temporalmente):
# spring.jpa.hibernate.ddl-auto=create
# spring.profiles.active=dev
```

**📝 Instrucciones:**

1. Si inicias el proyecto por primera vez:
   - Cambia `spring.jpa.hibernate.ddl-auto=validate` a `create`.
   - Activa el perfil de desarrollo: `spring.profiles.active=dev`.

2. Una vez creada la base de datos:
   - Regresa `ddl-auto` a `validate`.
   - Comenta o elimina el perfil `dev`.

---

## ▶️ Cómo levantar el entorno

1. Asegúrate de tener Docker y Docker Compose instalados.
2. Desde la carpeta `docker/`, ejecuta:

```bash
docker-compose up --build
```

Esto levantará los siguientes servicios:

| Servicio   | Puerto local | Descripción                     |
|------------|--------------|---------------------------------|
| MySQL      | 3307         | Base de datos                   |
| Backend    | 8080         | API REST en Spring Boot         |
| Frontend   | 3000         | Interfaz React + Nginx          |

---

## 🐳 Explicación de archivos Docker

### `docker-compose.yml`

Orquesta los servicios `mysql`, `backend`, `frontend` y `db-restore` (opcional). El backend depende del estado saludable de MySQL.

### `Dockerfile-api`

Define cómo se construye la imagen de la API:

```dockerfile
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### `Dockerfile-front`

Construye la app React y la sirve con Nginx:

```dockerfile
FROM node:20-alpine as build
WORKDIR /app
COPY memberflow-frontend .
RUN npm install && npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY docker/nginx.conf /etc/nginx/conf.d/default.conf
```

### `nginx.conf`

Permite rutas amigables con React Router:

```nginx
server {
  listen 80;
  server_name localhost;

  location / {
    root /usr/share/nginx/html;
    index index.html;
    try_files $uri /index.html;
  }
}
```

---

## 🔁 Portabilidad

Si se copia **todo el proyecto** (incluyendo `docker/mysql-data`), los contenedores se levantarán con todos los datos y configuraciones conservadas.  
No es necesario usar `backup.sql` si ya tienes los datos en la carpeta local.

---

## 🛠️ Requisitos del sistema

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- Java 21 (para desarrollo backend local)
- Node.js 20 (para desarrollo frontend local)

---

## ✅ Notas adicionales

- El servicio `db-restore` es opcional y solo útil si deseas restaurar una base de datos desde `backup.sql`.
- Puedes eliminar el contenido de `mysql-data/` si deseas reiniciar la base completamente.
- La API incluye Swagger disponible en: `http://localhost:8080/swagger-ui/index.html`

---

¡Gracias por usar MemberFlow!  
Para más información, revisa la carpeta `/Documentación` o abre un issue.