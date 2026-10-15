@echo off
cd /d "%~dp0"
echo Encendiendo PostgreSQL...
docker compose up -d
if errorlevel 1 (
  echo No se pudo encender PostgreSQL. Revisa que Docker este abierto.
  exit /b 1
)
echo Encendiendo el servicio de usuarios...
call mvnw.cmd spring-boot:run
