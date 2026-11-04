# Recipe Management System

A Spring Boot REST API for managing recipes, tags, reviews, and meal plans — built as a backend engineering showcase.

![Java](https://img.shields.io/badge/Java-21-007396?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-6DB33F?style=flat-square&logo=springboot)
![Maven](https://img.shields.io/badge/Maven-C71A1F?style=flat-square&logo=apachemaven)

---

## What it does

- **Recipes** — CRUD with validation, ingredients, instructions, categories, and tags
- **Smart search** — filter by included/excluded tags and free-text matching, with pagination
- **Ratings** — leave a score and review; the average rolls up onto the recipe
- **Meal planning** — structure weekly plans into daily meals by meal type [Not Implemented]
- **Accounts & roles** — registration, BCrypt passwords, role-gated write access
- **Caching** — Ehcache (JCache) with cache event logging

## Highlights

| Area | Detail |
|---|---|
| **Query design** | Composable JPA `Specification` predicates, tag include/exclude via `IN` subqueries |
| **API design** | HATEOAS links, RFC 7386 merge-patch, pagination via `X-Total-Count` headers |
| **Security** | Route-level authorization, HTTP Basic, BCrypt, per-owner write checks |
| **Performance** | Ehcache with explicit cache events, virtual threads, paginated queries |
| **Testing** | Integration tests against a live random-port server — auth, validation, CRUD |

## Architecture

```
boundary (REST) → service (logic) → repository (JPA) → entity
```

Feature modules are grouped by domain: `recipe/`, `review/`, `MealPlanner/`, `users/`, `config/`, `security/`.

## Tech Stack

Java 21 · Spring Boot 4.1 (Web, Data JPA, Security, Cache, HATEOAS, Validation) · H2 · Ehcache / JCache · JUnit 5 · Maven

## Run it

```bash
mvn spring-boot:run
```

API base: `http://localhost:8881/recipes/api` · H2 console: `/recipes/h2-console`

<details>
<summary>Sample endpoints</summary>

```
POST   /api/register
GET    /api/recipe?includedTags=1,2&excludeTags=3&name=pasta&offset=0&limit=5
GET    /api/recipe/{id}
POST   /api/recipe            # CHEF only
PUT    /api/recipe/{id}       # CHEF only
PATCH  /api/recipe/{id}       # merge-patch+json
DELETE /api/recipe/{id}
```

</details>

## Tests

```bash
mvn test
```

---

## Note on AI assistance

This `README.md` and the SQL seed data in [`src/main/resources/data.sql`](src/main/resources/data.sql) were written with AI assistance.

The application source code, JPA entities, database schema and project configuration are hand-written and maintained by the author.

---

*Built as a portfolio project — Java · Spring Boot · JPA · REST.*
