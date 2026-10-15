# Usuarios y autenticación — CampusStudy

Servicio de usuarios, registro, autenticación y autorización. Los demás servicios lo consultan para saber quién es el usuario y si su token es válido.

Base: `http://localhost:8081`

## 1. Instalar

Requisitos:

- Java 17 o superior
- Docker Desktop abierto
- Maven 3.9, o el wrapper del proyecto (`mvnw` / `mvnw.cmd`)

Clonar el repositorio y entrar a la carpeta del proyecto.

## 2. Configurar

La configuración está en `src/main/resources/application.properties`.

| Propiedad | Valor | Uso |
| --- | --- | --- |
| `server.port` | `8081` | Puerto del servicio |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5433/campusstudy_users` | Base PostgreSQL del servicio |
| `spring.datasource.username` | `postgres` | Usuario de PostgreSQL. No es un usuario de CampusStudy |
| `spring.datasource.password` | `postgres` | Contraseña de ese usuario de PostgreSQL. Cámbiala si en tu instalación es otra |
| `app.jwt.secret` | definida en el archivo | Clave con la que se firma el JWT. Mínimo 32 bytes |
| `app.jwt.expiration` | `86400000` | El token dura 24 horas |

Esas credenciales son las de PostgreSQL. Sirven para que el servicio se conecte a la base. No sirven para iniciar sesión en CampusStudy.

La tabla `users` está definida en `src/main/java/com/campusstudy/users_auth/database/schema.sql`. Al arrancar, el servicio la crea en `campusstudy_users` si todavía no existe. Los registros viven en PostgreSQL: al apagar el microservicio siguen guardados.

Si todavía no existen, al arrancar quedan estas cuentas:

| Email | Contraseña | Rol |
| --- | --- | --- |
| `estudiante@uniminuto.edu` | `Estudiante123` | estudiante |
| `docente@uniminuto.edu` | `Docente12345` | docente |
| `admin@uniminuto.edu` | `Admin12345` | administrador |

Esas tres cuentas son de prueba. Quien se registra usa su propio correo, su contraseña y uno de estos roles: `estudiante`, `docente` o `administrador`.

## 3. Ejecutar

Con Docker Desktop abierto, en la carpeta del proyecto:

```bash
iniciar.cmd
```

Ese comando enciende PostgreSQL y después el servicio. El servicio queda en `http://localhost:8081`.

Los usuarios quedan guardados en PostgreSQL, en la base `campusstudy_users`, tabla `users`. Cerrar el servicio no los borra. La próxima vez que se ejecute `iniciar.cmd` siguen ahí, con el mismo correo y la misma contraseña.

Para apagar solo la base, cuando el servicio ya esté cerrado:

```bash
docker compose stop
```

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

Devuelve el perfil de ese id. Cualquier usuario autenticado y activo puede consultarlo. Si no existe: `404`.

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

Sin token, o con un token inválido, las rutas de `/api/v1/usuarios` responden `401`. Si la cuenta del token no está activa, responden `403`.

## 5. Probar

Pruebas automáticas, desde la carpeta del proyecto:

```bash
mvnw.cmd test
```

Están en `tests/`. Usan la base `campusstudy_users_test` y no borran los usuarios de `campusstudy_users`.

Con el servicio en marcha, desde Postman o con curl:

```bash
curl -X POST http://localhost:8081/api/v1/auth/login -H "Content-Type: application/json" -d "{\"email\":\"admin@uniminuto.edu\",\"password\":\"Admin12345\"}"
```

Copiar el `token` y consultar el perfil:

```bash
curl http://localhost:8081/api/v1/usuarios/me -H "Authorization: Bearer <token>"
```

Para validar ese token desde otro servicio:

```bash
curl -X POST http://localhost:8081/api/v1/auth/validate -H "Content-Type: application/json" -d "{\"token\":\"<token>\"}"
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
