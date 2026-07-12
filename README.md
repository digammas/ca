# CookAlchemy

## In a nutshell
This is the code of a cookbook application, called CookAlchemy, or CA for short.
This is a monorepo composed of three directories:
* The backend, written in Kotlin and using Spring framework.
* The frontend, written in TypeScript and using Nuxt framework.
* The devenv, a folder containing the necessary configuration to run developer environment. In this case this is mainly
a docker compose file with config files for its containers.

### The backend
The backend code follows the hexagonal architecture, and is composed of several modules.
It is written in Kotlin and uses Spring framework.
It is a Maven project, and the modules are defined in the `pom.xml` file.

#### Domain module
The directory `domain` contains the domain modue code.
This module contains the domain model, and the business logic of the application.
It is split in three top packages: 
* `internal`: contains the code of the domain model and the business logic of the application. The domain part in a
hexagonal architecture.
* `spi`: contains ports on which the domain depends. This is the "left" part in a hexagonal architecture.
* `api`: contains ports that leverage the domain model. This is the "right" part in a hexagonal architecture.

#### Application module
The directory `app` contains the application module code.
This is a slim module that boot straps the application, and configures the dependencies of the domain module.

#### GraphQL module
The directory `graphql` contains the GraphQL module code.
This module contains the GraphQL API of the application, and is responsible for exposing the domain model.
It uses API port of the domain module.

#### Persistence module
The directory `persistence` contains the persistence module code.
This module contains the implementation of persistence SPI ports of the domain module.
Persistence is implemented using jOOQ. Data migrations are managed using Liquibase.
The underlying database engine is PostgreSQL.
The module uses testcontainers to run integration tests against a real PostgreSQL database.
It requires a running docker engine to run the tests.

## How to run this app as developer

Start developer environment services.
```
cd devenv
docker compose up
```

Run backend.
```
cd backend
mvn install
mvn spring-boot:run -pl app
```

Run frontend.
```
cd frontend/app
npm install
npm run dev
```

The application will be available on address http://localhost:3000/.