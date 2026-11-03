# 📋 Mini Task & Issue Tracker

A fullstack web application for tracking tasks and project issues, built with **Java Spring Boot 3**, **JUnit 5**, **ReactJS (Vite)**, and **Tailwind CSS**. Designed following the **3-Layer Architecture** and **RESTful API** best practices.

---

## 🚀 Tech Stack

### Backend
- **Java 17+** & **Spring Boot 3.x**
- **Spring Web** (RESTful APIs)
- **Spring Data JPA / Hibernate** (ORM & Data Persistence)
- **H2 Database** (In-memory relational database)
- **Bean Validation** (`jakarta.validation` for input validation)
- **Lombok** (Boilerplate code reduction)
- **JUnit 5** (Automated Unit & Service Layer Testing)

### Frontend
- **ReactJS 18** (with **Vite**)
- **Tailwind CSS** (Responsive Dashboard UI)
- **Axios** (HTTP Client for API integration)

### Tools & Testing
- **Postman** (Automated API Testing Collection covering status codes, response schemas, and boundary validations)
- **Maven** & **Git/GitHub**

---

## 🏗️ System Architecture (3-Layer Pattern)

```text
[React Frontend (Vite + Tailwind)]
               │
               ▼  HTTP RESTful JSON (Axios)
[Controller Layer]  ──> Receives requests, validates DTOs (@Valid), returns ResponseEntity
               │
               ▼
 [Service Layer]    ──> Handles business logic, default states, throws custom exceptions
               │
               ▼
[Repository Layer]  ──> Interfaces with Database via Spring Data JPA (JpaRepository)
               │
               ▼
    [(H2 Database)]
```

### Project Directory Structure
```text
mini-task-tracker/
├── backend/                 # Spring Boot 3 REST API & JUnit 5 Tests
├── frontend/                # React (Vite) + Tailwind CSS Single-Page Dashboard
├── docs/                    # Postman Collection, Test Cases & Coding Conventions
│   ├── CODING_CONVENTIONS.md
│   ├── TEST_CASES.md
│   └── postman_collection.json
└── README.md
```

---

## 🔌 RESTful API Endpoints

| Method | Endpoint | Description | Success Code | Error Codes |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/tasks` | Get all tasks (supports optional `?status=` filter) | `200 OK` | `400 Bad Request` |
| `GET` | `/api/tasks/{id}` | Get task details by ID | `200 OK` | `404 Not Found` |
| `POST` | `/api/tasks` | Create a new task | `201 Created` | `400 Bad Request` |
| `PUT` | `/api/tasks/{id}/status` | Update task status (`TODO`, `IN_PROGRESS`, `DONE`) | `200 OK` | `400` / `404` |
| `DELETE` | `/api/tasks/{id}` | Delete a task by ID | `204 No Content` | `404 Not Found` |

---

## ⚙️ Getting Started

### Prerequisites
- **JDK 17** or newer
- **Maven 3.8+**
- **Node.js 18+** & **npm**

### 1. Run Backend (Spring Boot)
```bash
cd backend
mvn spring-boot:run
```
- The backend server will start at: `http://localhost:8080`
- H2 Database Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:taskdb`, User: `sa`, Password: *empty*)

### 2. Run Automated Tests (JUnit 5)
```bash
cd backend
mvn test
```

### 3. Run Frontend (React + Vite)
```bash
cd frontend
npm install
npm run dev
```
- The frontend dashboard will run at: `http://localhost:5173`

---

## 🧪 Testing & Documentation
- **Coding Standards:** See [docs/CODING_CONVENTIONS.md](./docs/CODING_CONVENTIONS.md)
- **Test Cases (EP & BVA):** `docs/TEST_CASES.md`
- **Postman Collection:** Import `docs/postman_collection.json` into Postman to execute automated API tests.

