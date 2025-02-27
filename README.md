# Bank Entity Extraction API

![Java](https://img.shields.io/badge/Java-17-brightgreen)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-green)
![MongoDB](https://img.shields.io/badge/MongoDB-Latest-success)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)

A REST API service that extracts bank data from spreadsheets, processes SWIFT/BIC codes, and provides endpoints for data management.

## Table of Contents

1. [About](#about)
2. [Features](#features)
3. [Technologies](#technologies)
4. [Getting Started](#getting-started)

## About

This service provides functionality to process and manage SWIFT/BIC codes data. It extracts bank information from spreadsheets, stores it in a database, and makes it accessible through RESTful endpoints. The service handles both headquarters and branch offices, with special processing for their relationships.

## Features

Core functionality:
- [ ] SWIFT/BIC code data extraction from spreadsheets
- [ ] Automatic detection of headquarters (XXX-ending codes) and branch relationships
- [ ] Data persistence with MongoDB
- [ ] RESTful API for data access and management

Nice to have:
- [ ] Edge cases validation
- [ ] Comprehensive test coverage (unit & integration)
- [ ] Containerized deployment

## Technologies

- Java 17
- Spring Boot
- MongoDB
- Docker
- JUnit & Mockito for testing

## Getting Started

### Prerequisites

- Docker and Docker Compose
- Java 17 (for local development)

### Running with Docker

1. Clone the repository:
```bash
git clone https://github.com/yourusername/bank-entity-extraction-api.git
```
2. Navigate to cloned repository folder
```bash
cd bank-entity-extraction-api
```
3. Start the application:
```bash
docker-compose up
```

The API will be available at http://localhost:8080