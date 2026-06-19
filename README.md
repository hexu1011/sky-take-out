# Sky Take-Out (苍穹外卖) — Online Food Ordering System

A food-ordering system for catering businesses, consisting of a
**web admin backend** (employee, dish, set-meal & shop management) and a
**WeChat Mini-Program** for customers (browse, cart, order, payment).

Built with Spring Boot in a multi-module Maven architecture. The project
started as a learning project; I implemented the backend module by
module.

## Tech Stack
- **Backend:** Spring Boot, Spring MVC, MyBatis
- **Database / Cache:** MySQL, Redis (Spring Data Redis + Spring Cache)
- **Auth:** JWT + custom interceptor, ThreadLocal context
- **Others:** Spring AOP, Apache HttpClient, knife4j (Swagger UI)
- **Build:** Maven (multi-module)

## Architecture
sky-take-out (parent POM)
├─ sky-common   utils, constants, exceptions, unified result wrappers
├─ sky-pojo     entities, DTOs, VOs
└─ sky-server   controllers, services, mappers, config, application entry

## Features
### Admin (Web)
- Employee: login (JWT), CRUD, enable/disable
- Category / Dish / Set-meal management (CRUD, enable/disable, image upload)
- Shop business status (open/closed)

### Customer (WeChat Mini-Program)
- WeChat login with auto-registration of new users
- Browse dishes & set-meals (cached via Redis / Spring Cache)
- Shopping cart and address book
- Place order & pay via WeChat Pay


## Highlights
- **Auto-fill of common DB fields** (create/update time & user) via
  **Spring AOP + a custom annotation + reflection**, removing repetitive
  boilerplate across all service methods.
- **Caching** of dish/set-meal data with **Redis & Spring Cache**, with cache
  invalidation on admin updates to keep DB and cache consistent.
- **Stateless authentication** with **JWT + a custom interceptor**; the resolved
  user id is passed across layers via **ThreadLocal**.
- **WeChat integration** for login & payment via **HttpClient**
  (jscode2session, JSAPI order, payment callback).

## My Extensions (beyond the base project)

## Getting Started
### Run
1. Create the database and import `sky.sql`.
2. Configure `sky-server/src/main/resources/application-dev.yml`
   (datasource, Redis, WeChat keys).
3. Start the `sky-server` module: `mvn spring-boot:run`.
4. Open the API docs at `http://localhost:8080/doc.html`.

## API Documentation
Swagger / knife4j: `http://localhost:8080/doc.html`

## Note
This repository is for learning and portfolio purposes.
