# BMS

**BMS** is a multi-event booking platform that allows users to browse and book tickets for **Movies, Sports, and Concerts** through a unified system.

---

## Features

### User Features
- Browse events (Movies, Sports, Concerts)
- View event & show details
- Seat selection & ticket booking
- Booking history
- Cancel bookings

### Admin Features
- Manage events & shows
- Manage venues and seating
- Generate reports

### System Highlights
- Supports multiple event types
- Dynamic seat management
- Extensible event architecture
- Add-on support (VIP, combos, etc.)

---

## Tech Stack

### Backend
- **Java 21**
- **Spring Boot**
- **Spring MVC**
- **Hibernate (JPA)**
- **Thymeleaf**
- **HTMX**

### Database
- **H2** (in-memory database)
- **PostgreSQL** 

### DevOps & Deployment
- **Docker** 
- **GitHub Actions** 
- **Render** 

---

## Project Structure

```text
BMS/
│
├── src/main/java/      # Application source code
├── src/main/resources/
│   ├── templates/      # Thymeleaf templates
│   └── static/         # CSS, JS, assets
│
├── Dockerfile
├── pom.xml
└── .github/workflows/  # CI pipeline
```

---

## Getting Started

### Clone Repository
```bash
git clone https://github.com/<your-username>/BMS.git
cd BMS
```

### Run Application (Development)
```bash
mvn clean
mvn spring-boot:run
```
The application will be available at: [http://localhost:8080](http://localhost:8080)

### H2 Database Console (Dev Only)
- **URL:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL:** `jdbc:h2:mem:BMS`
- **User:** `sa`
- **Password:** (leave blank)

---

## Running with Docker

### Build Image
```bash
docker build -t BMS .
```

### Run Container
```bash
docker run -p 8080:8080 BMS
```

---

## CI Pipeline

GitHub Actions automatically performs the following on every push and pull request:
- Builds the project
- Runs tests
- Builds Docker image

---

##  Architecture Overview

BMS follows the **MVC architecture**:
- **Controller**: Handles HTTP requests.
- **Service**: Contains business logic.
- **Repository**: Manages data persistence.
- **View**: Thymeleaf + HTMX for dynamic rendering.

---

## Design Goals
- Clean and modular architecture.
- Extensible event type system.
- Maintainable service-layer logic.
- Ready for scaling & cloud deployment.

---

##  Future Enhancements
- PostgreSQL integration.
- Payment gateway integration.
- Real-time seat locking.
- Notifications system.
- Role-based authentication.
- Dynamic pricing engine.

