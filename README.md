## Running with Docker Compose

From the repository root you can start the entire application stack with:

```bash
docker compose up --build
```

This will build the Angular frontend and start the backend together with a
PostgreSQL database. Once the services are up you can reach the application at
`http://localhost:8080/petclinic/` and the REST API at
`http://localhost:9966/petclinic/api`.

The database is exposed on port `5433` if you want to connect to it directly.

---

The following steps describe how to build and run the containers manually if
you prefer to run them separately.

### petclinic FE

```bash
cd demo-spring-petclinic-angular
docker build -t pet-fe .
docker run -p 8080:8080 pet-fe
```

### petclinic BE

```bash
cd demo-spring-petclinic-java
docker compose --profile postgres up --build
mvn spring-boot:run
```

The APIs will be exposed from: `http://localhost:9966/petclinic/api`
