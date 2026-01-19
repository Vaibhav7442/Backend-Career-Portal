# Career Portal - Backend

Spring Boot RESTful API backend for the Career Portal platform.

## 🎯 Features

- **RESTful API** - Clean and well-structured REST endpoints
- **JWT Authentication** - Secure token-based authentication
- **Role-Based Access Control** - Separate permissions for job seekers and employers
- **File Management** - Resume and photo upload/download functionality
- **Database Integration** - MySQL with JPA/Hibernate
- **Input Validation** - Request validation using Bean Validation
- **Exception Handling** - Centralized error handling
- **CORS Configuration** - Configured for frontend integration

## 🛠️ Technology Stack

- **Spring Boot** 3.5.7 - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data persistence
- **MySQL** - Relational database
- **JWT (jjwt)** 0.11.5 - JSON Web Token implementation
- **Hibernate** - ORM framework
- **Maven** - Build and dependency management
- **Java** 17 - Programming language

## 📁 Project Structure

```
career-portal-backend/
├── src/main/java/com/careerportal/career_portal_backend/
│   ├── config/              # Configuration classes
│   │   ├── SecurityConfig.java
│   │   └── WebMvcConfig.java
│   ├── controller/          # REST controllers
│   │   ├── AuthController.java
│   │   ├── JobPostingController.java
│   │   ├── ApplicationController.java
│   │   ├── JobSeekerController.java
│   │   └── EmployerController.java
│   ├── entity/              # JPA entities
│   │   ├── User.java
│   │   ├── JobPosting.java
│   │   ├── Application.java
│   │   ├── JobSeekerProfile.java
│   │   ├── Employer.java
│   │   └── Role.java
│   ├── repository/          # Data access layer
│   │   ├── UserRepository.java
│   │   ├── JobPostingRepository.java
│   │   └── ...
│   ├── service/             # Business logic
│   │   ├── JobPostingService.java
│   │   ├── ApplicationService.java
│   │   ├── EmployerService.java
│   │   └── ...
│   ├── security/            # Security components
│   │   ├── JwtTokenProvider.java
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── JwtAuthenticationEntryPoint.java
│   │   └── CustomUserDetailsService.java
│   ├── payload/             # DTOs
│   │   ├── LoginDto.java
│   │   ├── JobPostingDto.java
│   │   └── ...
│   ├── exception/           # Custom exceptions
│   │   ├── ResourceNotFoundException.java
│   │   └── AccessDeniedException.java
│   └── CareerPortalBackendApplication.java
├── src/main/resources/
│   ├── application.properties
│   └── ...
├── uploads/                 # File storage directory
├── pom.xml
└── README.md
