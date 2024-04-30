# Task Management System

This is system for tasks.

## Table of Contents

1. [Installation](#installation)
2. [Getting Started](#getting-started)
2. [Built With](#built-with)

## Installation

```
git clone https://github.com/nurananacafova/TaskManagement.git
```

## Getting Started

* Run the project. Open the below link in the browser.

```
http://localhost:8080/swagger-ui/index.html
```
* Create image:

```
docker build -t task_management_img:v12.3 .
```
* Run docker-compose.yml:

```
docker compose up
```

Note: There are "ADMIN" and "USER" roles. Default role is "ADMIN"

When running the project for the first time, first create organizations by logging in as "ADMIN".
Once the organizations are created, you can enter organization IDs as "USER".
Because it is not necessary to enter the organization id if you are "ADMIN" when registering. However, if you are a "
USER", the organization id must be entered.

## Built With

- [Spring Boot](https://spring.io/projects/spring-boot) - Framework for building Java applications.
- [MySQL](https://www.mysql.com/) - Open-source relational database.
- [Maven](https://maven.apache.org/) - Project management build tool
- [Apache Kafka](https://kafka.apache.org/) - Distributed event streaming platform.
- [Docker](https://www.docker.com/#build) - Open platform for developing, shipping, and running applications.

