# Sky Take-Out (苍穹外卖) — Online Food Ordering System

A food-ordering system for catering businesses: a **web admin backend** (employee,
dish, set-meal & shop management) and a **React web client** for customers to browse,
add to cart and order. It began as a Spring Boot course project; I built on the basics with a few extensions of my own
— modernizing the whole stack and building the web client from scratch.

Built with Spring Boot in a multi-module Maven architecture.

## Tech Stack

**Backend**
- Spring Boot 3.3.7, Spring MVC, MyBatis 3 — on **Java 21**
- MySQL 8, Redis (Spring Data Redis + Spring Cache)
- **Flyway** (versioned database migrations)
- Auth: JWT + custom interceptor, ThreadLocal context, **BCrypt** password hashing
- knife4j (**OpenAPI 3**), Apache HttpClient
- Maven (multi-module)

**Frontend (customer web client)**
- React + TypeScript, Vite
- Mantine (UI), React Router, Axios

## Architecture

```
sky-take-out (parent POM)
├─ sky-common   utils, constants, exceptions, unified result wrappers
├─ sky-pojo     entities, DTOs, VOs
├─ sky-server   controllers, services, mappers, config, app entry
│               + Flyway migrations (resources/db/migration)
└─ sky-web      React + TypeScript customer web client (Vite)
```

## Features

**Customer — React Web Client**
- Email / password registration & login (BCrypt + JWT)
- Browse dishes & set-meals by category (cached via Redis / Spring Cache)
- Shopping cart: live item-count badge, per-item quantity controls

**Admin (Web)**
- Employee: login (JWT), CRUD, enable/disable
- Category / Dish / Set-meal management (CRUD, enable/disable, local image upload)
- Shop business status (open/closed)

> A legacy WeChat Mini-Program client (login, cart, order, WeChat Pay) from the original
> course project also remains in the codebase.

## Highlights
- **Stateless JWT authentication** with a custom Spring MVC interceptor; the resolved
  user id is carried across layers via a ThreadLocal context.
- **Redis / Spring Cache** for dish & set-meal data, with cache invalidation on admin
  updates to keep DB and cache consistent.
- Unified API response envelope and centralized exception handling across all endpoints.
- Auto-fill of audit fields (create/update time & user) via a Spring AOP aspect.

## What I Added Beyond the Base Course Project
- **Upgraded Spring Boot 2.7 → 3.3.7 and JDK → 21**: full `javax → jakarta` migration,
  dependency modernization (MyBatis 3, Druid SB3 starter, PageHelper 2, POI 5),
  rewrote the JWT util for jjwt 0.12, migrated Swagger 2 → OpenAPI 3 (knife4j).
- **Introduced Flyway** for versioned, reproducible schema migrations (baseline + incremental).
- **Removed Alibaba Cloud OSS**; switched image upload to local disk storage.
- **Built a standalone React + TypeScript + Mantine web client** — menu browsing,
  email/password auth, shopping cart — on a typed Axios layer with JWT handling and CORS.
- Added an **email/password account system** (BCrypt) alongside the original WeChat login.

## Getting Started

**Backend**
1. Create an empty MySQL database `sky_take_out`.
2. Configure `sky-server/src/main/resources/application-dev.yml` (datasource, Redis).
3. Run `mvn -pl sky-server spring-boot:run` — **Flyway builds the schema on first start**.
   (Load seed data such as an admin account and sample dishes as needed.)
4. API docs: `http://localhost:8080/doc.html`

**Frontend** (`sky-web/`)
1. `npm install`
2. `npm run dev` → open `http://localhost:5173`

## API Documentation
knife4j (OpenAPI 3): `http://localhost:8080/doc.html`

## Note
This repository is for learning and portfolio purposes.
