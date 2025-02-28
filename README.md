# Bank Entity Extraction API

![Java](https://img.shields.io/badge/Java-19-brightgreen)
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
- [x] SWIFT/BIC code data extraction from spreadsheets
- [x] Automatic detection of headquarters (XXX-ending codes) and branch relationships
- [x] Data persistence with MongoDB
- [x] RESTful API for data access and management

Nice to have:
- [x] Edge cases validation
- [x] Comprehensive test coverage (unit & integration)
- [x] Containerized deployment

## Technologies

- Java 17
- Spring Boot
- MongoDB
- Docker
- JUnit & Mockito for testing

## Getting Started

### Prerequisites

- Docker and Docker Compose
- Java 19 (for local development)

### Running with Docker

1. Clone the repository:
```bash
git clone https://github.com/newmon13/swift-rest-api.git
```
2. Navigate to cloned repository folder
```bash
cd swift-rest-api
```
3. Start the application:
```bash
docker-compose up
```

The API will be available at http://localhost:8080
Documentation for API is available under http://localhost:8080/swagger-ui/index.html#/

Example File with banks can be uploaded via swagger

### Uploading spreadsheet data

1. Go to http://localhost:8080/swagger-ui/index.html#/
2. Press 'Try it out'
![image](https://github.com/user-attachments/assets/660edaa7-b6e2-40fe-af5c-4efb9f94f06a)
3. Select proper menu option according to spreadsheet structure (true -> will skip first row that has headers row)
![image](https://github.com/user-attachments/assets/2c282826-4c03-4ce2-bfa0-6c2dc0708387)
4. Select file and press 'Execute'
![image](https://github.com/user-attachments/assets/c7d46bc2-b1cd-4fcb-b664-8c4faba88d73)

