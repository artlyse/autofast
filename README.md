# 🚗 AutoFast - Sistema de Gestión para Taller Mecánico

[![Java](https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-005F0F?logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![Render](https://img.shields.io/badge/Render-Deployed-46E3B7?logo=render&logoColor=white)](https://render.com)

---

## 📋 Descripción

**AutoFast** es un sistema de información integral diseñado para el taller mecánico *Multiservicios Suministros AutoFast S.A.C.* Su objetivo principal es centralizar la gestión de clientes, vehículos, órdenes de servicio, diagnósticos, reparaciones, repuestos e inventario, eliminando la dispersión de información y mejorando la trazabilidad de cada atención.

El sistema implementa **autenticación por roles** (Administrador, Recepcionista, Técnico y Almacenero), **registro de auditoría** de todas las acciones, y una interfaz web accesible y responsiva.

---

## ✨ Características principales

- ✅ **Gestión de clientes** – CRUD completo con validación de DNI/RUC único.
- ✅ **Gestión de vehículos** – Asociados a clientes, con placa única.
- ✅ **Órdenes de servicio** – Generación con número único, asignación de técnico, seguimiento de estado (Pendiente, Diagnosticado, En reparación, Finalizado).
- ✅ **Diagnóstico y reparación** – Registro de problemas, diagnóstico y trabajo realizado.
- ✅ **Inventario de repuestos** – Control de stock, stock mínimo y alertas.
- ✅ **Gestión de usuarios** – Solo administrador, con asignación de roles y sincronización automática con técnicos.
- ✅ **Auditoría completa** – Registro de cada acción con usuario, IP y fecha.
- ✅ **Reportes** – Estadísticas y resúmenes para la toma de decisiones.
- ✅ **Roles y permisos** – Spring Security con acceso restringido según perfil.
- ✅ **Diseño accesible** – Navegación por teclado, contraste y mensajes claros.

---

## 🛠️ Tecnologías utilizadas

| Capa | Tecnología |
|------|------------|
| **Backend** | Java 17, Spring Boot 3.2.0, Spring Data JPA, Spring Security |
| **Frontend** | Thymeleaf, HTML5, CSS3 |
| **Base de datos** | MySQL 8.0 (local), TiDB (producción) |
| **Seguridad** | BCrypt, roles, auditoría con AOP |
| **Herramientas** | Maven, Git, Docker |
| **Despliegue** | Render (Web Service con Docker) |

---

## 🏗️ Arquitectura

Presentación (Thymeleaf)
↓
Controlador (MVC)
↓
Servicio (Negocio – Validaciones y reglas)
↓
DAO (Spring Data JPA)
↓
Base de datos (MySQL / TiDB)


Patrones aplicados:
- **MVC** – Separación de vistas, controladores y modelo.
- **DAO** – Encapsulamiento del acceso a datos.
- **Servicio** – Lógica de negocio centralizada.
- **DTO** – Transferencia de datos entre capas.

---

## 📁 Estructura del proyecto
autofast/
├── src/
│ ├── main/
│ │ ├── java/pe/edu/utp/autofast/
│ │ │ ├── config/ # Configuraciones (Security, DataLoader)
│ │ │ ├── controller/ # Controladores MVC
│ │ │ ├── service/ # Lógica de negocio
│ │ │ ├── repository/ # Repositorios JPA
│ │ │ ├── entity/ # Entidades JPA
│ │ │ ├── dto/ # Objetos de transferencia
│ │ │ └── aspect/ # AOP para auditoría
│ │ └── resources/
│ │ ├── templates/ # Vistas Thymeleaf
│ │ │ ├── layout/ # Plantilla base
│ │ │ ├── dashboard/ # Panel de control
│ │ │ ├── clientes/ # CRUD clientes
│ │ │ ├── vehiculos/ # CRUD vehículos
│ │ │ ├── ordenes/ # CRUD órdenes + detalle
│ │ │ ├── inventario/ # CRUD repuestos
│ │ │ ├── usuarios/ # Gestión de usuarios (admin)
│ │ │ ├── auditoria/ # Historial de acciones
│ │ │ └── reportes/ # Estadísticas
│ │ ├── static/css/ # Estilos CSS
│ │ └── application.properties # Configuración local
│ └── resources/
└── pom.xml # Dependencias Maven


---

## 🚀 Instalación y ejecución local

### Requisitos previos

- Java 17 o superior
- Maven 3.8+
- MySQL 8.0 (o TiDB para producción)
- Git

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/artlyse/autofast.git
cd autofast

# 2. Crear la base de datos en MySQL
CREATE DATABASE autofast_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 3. Configurar application.properties con tus credenciales
# Editar src/main/resources/application.properties

# 4. Ejecutar la aplicación
mvn clean spring-boot:run

# 5. Acceder en el navegador
http://localhost:8080

🔒 Seguridad y auditoría
Spring Security con autenticación basada en roles.

Contraseñas encriptadas con BCrypt.

Auditoría automática con AOP: cada creación, actualización, eliminación y cambio de estado se registra en la tabla auditoria con usuario, IP y fecha.

Acceso restringido por URL según rol.

👥 Autores
Chacon Mayta, Frans Rooswvelt

Ramos Marcas, Alberit Victor

Lozano Barboza, Yeferson Deyvin

Egusquiza Calva, Adrian Gabriel

Curso: Análisis y Diseño de Sistemas de Información
Docente: Ing. Oscar Enrique Osores Granda
Sección: 18358
Universidad: Universidad Tecnológica del Perú (UTP)

El sistema sigue una arquitectura en capas:
