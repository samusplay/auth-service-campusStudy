# Usuarios y autenticación — CampusStudy

Servicio de usuarios, registro, autenticación y autorización. Los demás servicios lo consultan para saber quién es el usuario y si su token es válido.

Base: `http://localhost:8081`

## 1. Instalar

En la terminal de Visual Studio Code, en la carpeta donde está `pom.xml`. Los comandos están en `requirements.txt`.

En Windows, el primero instala Java 17, el segundo Docker Desktop y el tercero las librerías del servicio:

```bash
winget install -e --id Microsoft.OpenJDK.17
winget install -e --id Docker.DockerDesktop
mvnw.cmd -DskipTests compile
```

En Git Bash, Mac o Linux:

```bash
brew install openjdk@17
brew install --cask docker
./mvnw -DskipTests compile
```

PostgreSQL corre dentro de Docker. Al levantar el contenedor queda la base `campusstudy_users`. Los usuarios quedan en la tabla `users`.

## 2. Configurar

La configuración está en `src/main/resources/application.properties`.

| Propiedad | Valor | Uso |
| --- | --- | --- |
| `server.port` | `8081` | Puerto del servicio |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5433/campusstudy_users` | Base PostgreSQL del servicio |
| `spring.datasource.username` | `postgres` | Usuario de PostgreSQL |
| `spring.datasource.password` | `postgres` | Contraseña de PostgreSQL |
| `app.jwt.secret` | definida en el archivo | Clave con la que se firma el JWT |
| `app.jwt.expiration` | `86400000` | El token dura 24 horas |

Esas credenciales son las de PostgreSQL. El servicio las usa para conectarse a la base. El front y los demás servicios entran por `http://localhost:8081`.

La tabla `users` está definida en `src/main/java/com/campusstudy/users_auth/database/schema.sql`. Al arrancar, el servicio la crea en `campusstudy_users`. Los registros viven en PostgreSQL: al apagar el microservicio siguen guardados.

Al arrancar quedan estas cuentas de prueba:

| Email | Contraseña | Rol |
| --- | --- | --- |
| `estudiante@uniminuto.edu` | `Estudiante123` | estudiante |
| `docente@uniminuto.edu` | `Docente12345` | docente |
| `admin@uniminuto.edu` | `Admin12345` | administrador |

Esas tres cuentas son de prueba. Quien se registra usa su propio correo, su contraseña y uno de estos roles: `estudiante`, `docente` o `administrador`.

## 3. Ejecutar

En la carpeta del proyecto.

En Windows (cmd o PowerShell):

```bash
docker desktop start
docker compose up -d
mvnw.cmd spring-boot:run
```

En Git Bash, Mac o Linux:

```bash
docker desktop start
docker compose up -d
./mvnw spring-boot:run
```

`docker desktop start` abre Docker Desktop si está cerrado. Si ya está abierto, sigue. `docker compose up -d` enciende PostgreSQL. `mvnw` enciende el servicio en `http://localhost:8081`.

El front hace login y guarda el `token`. En las rutas de usuarios el header va así, sin comillas y con un espacio después de `Bearer`:

```
Authorization: Bearer <token>
```

El id en la ruta es el número, por ejemplo `http://localhost:8081/api/v1/usuarios/1`.

Los usuarios quedan guardados en PostgreSQL, en la base `campusstudy_users`, tabla `users`. Cerrar el servicio no los borra.

## 4. Endpoints

Registro, login y validación del token no piden autorización. Para consultar usuarios hay que enviar el token de una cuenta activa:

```
Authorization: Bearer <token>
```

Otro servicio valida el token con `POST /api/v1/auth/validate` y, si necesita el perfil, llama a `GET /api/v1/usuarios/{id}` con ese mismo token.

### POST `/api/v1/auth/registro`

Crea un usuario. Responde `201`.

```json
{
  "nombre": "Ana Perez",
  "email": "ana@uniminuto.edu",
  "password": "clave1234",
  "rol": "estudiante"
}
```

`rol` puede ser `estudiante`, `docente` o `administrador`. La contraseña debe tener al menos 8 caracteres. Si el correo ya existe, responde `409`. Si el rol no es uno de esos tres, responde `400`.

### POST `/api/v1/auth/login`

Valida el correo y la contraseña y devuelve el token. Responde `200`.

```json
{
  "email": "ana@uniminuto.edu",
  "password": "clave1234"
}
```

```json
{
  "token": "<jwt>",
  "usuario_id": 1,
  "email": "ana@uniminuto.edu",
  "rol": "estudiante"
}
```

Credenciales incorrectas: `401`. Cuenta inactiva: `403`.

### POST `/api/v1/auth/validate`

Comprueba si el token es válido y devuelve el usuario y el rol. Lo usan los demás servicios.

```json
{
  "token": "<jwt>"
}
```

```json
{
  "valido": true,
  "usuario_id": 1,
  "email": "ana@uniminuto.edu",
  "rol": "estudiante"
}
```

Token inválido o vencido: `401`.

### GET `/api/v1/usuarios/me`

Devuelve el perfil del usuario dueño del token.

### GET `/api/v1/usuarios/{id}`

Devuelve el perfil de ese id. En la dirección va el número, por ejemplo `/api/v1/usuarios/1`. Cualquier usuario autenticado y activo puede consultarlo. Si no existe: `404`.

```json
{
  "usuario_id": 1,
  "nombre": "Ana Perez",
  "email": "ana@uniminuto.edu",
  "rol": "estudiante",
  "estado": "activo",
  "fecha_registro": "2026-10-14"
}
```

### GET `/api/v1/usuarios`

Lista todos los usuarios. Solo el rol `administrador` puede usarlo. Un estudiante o un docente recibe `403`.

Estadística pide solo los estudiantes con `GET /api/v1/usuarios?rol=estudiante` y el token del administrador. Responde `200` con ese listado.

Sin token, o con un token inválido, las rutas de `/api/v1/usuarios` responden `401`. Si la cuenta del token no está activa, responden `403`.

## 5. Probar

Pruebas automáticas, con la base encendida, desde la carpeta del proyecto.

En Windows:

```bash
mvnw.cmd test
```

En Git Bash, Mac o Linux:

```bash
./mvnw test
```

Están en `tests/` y usan la base `campusstudy_users`.

Con el servicio en marcha, desde Postman:

`POST http://localhost:8081/api/v1/auth/login`

Header: `Content-Type: application/json`

```json
{
  "email": "admin@uniminuto.edu",
  "password": "Admin12345"
}
```

Copiar el `token` y consultar el perfil:

`GET http://localhost:8081/api/v1/usuarios/me`

Header: `Authorization: Bearer <token>`

Para validar ese token desde otro servicio:

`POST http://localhost:8081/api/v1/auth/validate`

```json
{
  "token": "<token>"
}
```

## 6. Dependencias

Están en `pom.xml`. Spring Boot `3.2.5`.

| Dependencia | Uso |
| --- | --- |
| `spring-boot-starter-web` | API REST |
| `spring-boot-starter-validation` | Validación de registro y login |
| `spring-boot-starter-data-jpa` | Guardar y consultar usuarios |
| `postgresql` | Base de datos PostgreSQL. Ahí quedan guardados los usuarios |
| `spring-security-crypto` | Contraseñas cifradas con BCrypt |
| `jjwt-api`, `jjwt-impl`, `jjwt-jackson` `0.12.6` | Crear y validar el JWT |
| `lombok` | Constructores y builders de los DTO |
| `spring-boot-starter-test` | Pruebas |
