# 📝 Task Manager API (Spring Boot)

## 🚀 Overview

A RESTful backend application built using Spring Boot that allows users to manage tasks with full CRUD functionality, validation, pagination, and proper error handling.

---

## 🛠️ Tech Stack

* Java
* Spring Boot
* Spring Data JPA
* MySQL
* Hibernate
* Maven

---

## ✨ Features

* Create, Read, Update, Delete Tasks
* DTO-based architecture
* Global Exception Handling
* Validation with field-level error messages
* Pagination & Sorting
* Clean layered architecture (Controller, Service, Repository)

---

## 📂 Project Structure

controller → Handles API requests
service → Business logic
repository → Database interaction
model → Entity classes
dto → Data Transfer Objects
mapper → DTO ↔ Entity conversion
exception → Custom exception handling

---

## 📡 API Endpoints

### ➕ Create Task

POST /tasks

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

Structured error responses:

* 404 Not Found
* 400 Bad Request (Validation errors)

---

## 🧪 Sample Validation Error

{
"timestamp": "...",
"status": 400,
"error": "Bad Request",
"errors": {
"title": "Title is required"
}
}

---

## ▶️ Run the Project

1. Clone the repository
2. Configure MySQL in application.properties
3. Run the Spring Boot application

---

## 📌 Future Improvements

* JWT Authentication
* User-specific tasks
* Deployment (Render / Railway)
