# 🌆 BetterCity

**A microservice-based platform for managing urban issue reports**

---

## 📘 Project Overview

**BetterCity** is a modern platform designed to enhance the quality of life in cities by leveraging data analysis and providing intuitive tools for both residents and city officials.

The project is built as a set of backend microservices using Java and Spring Boot. The core idea is to empower users with a mobile app (planned with React Native + Expo for cross-platform support), allowing them to quickly report issues they encounter in the city.

## 📱 Example Reports

Using the app, residents will be able to take photos and report common urban problems, such as:

- 🌿 Fallen branches blocking the road
- 🕳️ Potholes in the asphalt
- 💡 Broken or non-functional streetlights
- 🕳️ Loose or damaged manhole covers
- And other hazards or maintenance issues

## 🏢 Application for City Officials

City employees and officials will have access to a dedicated web application featuring an interactive map. On this map, pins will be placed according to the exact locations of the reported issues, accompanied by photos submitted by residents. This tool will facilitate efficient monitoring and management of city maintenance tasks.

---

Stay tuned — more features and services are planned as the platform grows!


## Features

- **User Authentication**: Secure login and registration for users using JWT.
- **Photo Upload**: Users can upload photos of issues in the city.
- **Issue Reporting**: Users can report issues with descriptions and locations.

### In the future:

- **Admin Dashboard**: A web interface for city officials to manage reported issues.
- **Notifications**: Users receive notifications about the status of their reported issues.
- **Analytics**: Data analysis to identify trends and prioritize issues.
- **Community Engagement**: Users can comment on and vote for issues to prioritize them.

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.5
- **Database**: PostgreSQL
- **Authentication**: JWT
- **Deployment**: Docker for containerization
- **API Documentation**: Swagger for API documentation
- **Testing**: JUnit 5 for unit and integration testing
- **Frontend**: React Native + Expo (planned)
- **Deployment2**: Kubernetes for orchestration (planned)

# AuthService – Endpoint Collection

This collection contains a set of HTTP requests to the microservice responsible for user authentication and authorization.

> Required environment variables:
> - `AuthServiceBaseURL` – the microservice's base URL (e.g., `http://localhost:8080`)
> - `JWT_USER` – the logged-in user's JWT token
> - `JWT_UV_EMPLOYEE` – the unauthorized employee's JWT token
> - `JWT_EMPLOYEE` – the employee's JWT token with verification permissions

---

## Registration

### User registration

- **Method:** `POST`
- **Endpoint:** `/auth/register`
- **Body (JSON):**
```json
{
"email": "user@domain.pl",
"password": "Password123!",
"role": "USER"
}
```
---
### Unverified Employee Registration

* Method: POST
* Endpoint: /auth/register
```json
{
"email": "unverified.employee@domain.pl",
"password": "Password123!",
"role": "UNVERIFIED_EMPLOYEE"
}
```
---
## Login

### User Login

* Method: POST
* Endpoint: /auth/login
```json
{
"email": "user@domain.pl",
"password": "Password123!"
}
```
---
### Validating Tokens

#### Validating a User's JWT

### `GET /auth/validate`
* Authorization: Bearer {{JWT_USER}}
---
#### Validating an Unverified Employee's JWT

### `GET /auth/validate`

Authorization: Bearer {{JWT_UV_EMPLOYEE}}

---

### Changing Employee Status to Verified

### `PATCH /auth/verify-employee/{unverifiedEmployeeId}`

Authorization: Bearer {{JWT_EMPLOYEE}}

---

## Creating a Report

### `POST /reports`

- **Authorization:** `Bearer {{JWT_UV_EMPLOYEE}}`
- **Body (form-data):**
- `request`: JSON as `text`
```json
{
"description": "Broken bus stop",
"latitude": 50.06143,
"longitude": 19.93658
}
```
- `images`: files (e.g., photos of the report)

---

## Downloading reports

### `GET /reports`

- **Authorization:** none
- Returns a list of all reports.

---

## Downloading a single report

### `GET /reports/{id}`

- **Authorization:** none
- Parameter `id`: report ID (e.g., `1`)
- Returns details of a single report.

---

## Retrieving report images

### `GET /reports/{id}/images`

- **Authorization:** none
- Parameter `id`: report ID
- Returns a list of images associated with a given report.

---

## Deleting a report

### `DELETE /reports/{id}`

- **Authorization:** none
- Parameter `id`: report ID
- Deletes the specified report from the system.

---

## Updating a report

### `PATCH /reports/{id}`

- **Authorization:** none
- Parameter `id`: report ID
- **Body (JSON):**
```json
{
"description": "new",
"longitude": 1.45,
"latitude": 1.23
}
```