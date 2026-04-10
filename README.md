# 🚀 Task Manager Backend (Spring Boot + JWT Security)

## 📌 Overview

A production-ready Task Manager REST API built using Spring Boot.
The application supports secure authentication using JWT, role-based authorization, and user-specific data access.

---

## 🛠️ Tech Stack

* Java
* Spring Boot
* Spring Security
* JWT (JSON Web Token)
* Spring Data JPA
* MySQL
* Hibernate
* Maven
* Swagger (OpenAPI)

---

# ✨ Features

## 🔐 Authentication & Security

* JWT-based authentication (stateless)
* Custom JWT filter for request validation
* Secure login endpoint

## 🛡 Authorization & Data Ownership

* Role-based access control (USER / ADMIN)
* Admin can access all tasks 
* Users can only access their own tasks 
* Ownership enforced at service layer

## 📋 Task Management

* Create, Read, Update, Delete Tasks
* Pagination & sorting using Pageable

## 📦 Clean API Design

* DTO-based architecture
* API response wrapper for consistent responses
* Global exception handling with structured error responses
* Validation with field-level error messages

## 📊 API Documentation

* Swagger UI integrated
* JWT authorization supported in Swagger

## 🧱 Architecture

### Controller → Service → Repository → Database

* Controller → Handles API requests
* Service → Business logic & security checks
* Repository → Database interaction
* Security Layer → JWT authentication & authorization

## 🔑 Authentication Flow

1. User logs in via /auth/login
2. Server returns JWT token
3. Client sends token in header:
4. Authorization: Bearer
5. JWT filter validates token for each request
6. Access granted based on role and ownership

## 📡 API Endpoints

### 🔐 Auth

* POST /auth/register
* POST /auth/login

### 📋 Get All Tasks (Pagination + Sorting)

GET /tasks?page=0&size=5&sortBy=id

### 🔍 Get Task by ID

GET /tasks/{id}

### ✏️ Update Task

PUT /tasks/{id}

### ❌ Delete Task

DELETE /tasks/{id}

---

## ⚠️ Error Handling

Standard error response format:

{  
"timestamp": "...",  
"status": 404,  
"error": "Not Found",  
"message": "...",  
"path": "/tasks/1"  
}

---

## ▶️ Run the Project

1. Clone the repository
2. git clone https://github.com/dbybek/TaskManagerApplication.git
3. Navigate to project
4. cd TaskManagerApplication
5. Configure database in application.properties
6. Build project
7. mvn clean install
8. Run application
9. mvn spring-boot

---

## 🌐 API Testing (Swagger)

URL:
http://localhost:8080/swagger-ui.html

Steps:
1. Call /auth/login
2. Copy JWT token
3. Click Authorize
4. Enter: Bearer
5. Access secured APIs

---

## 📌 Future Improvements

* Task search & filtering
* Refresh token implementation
* Rate limiting
* Unit & integration testing

---

## 👨‍💻 Author

Bibek Swain
