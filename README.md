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

#### Domain module
This module contains the domain model, and the business logic of the application. It is split in three top packages: 
* `internal`: contains the code of the domain model and the business logic of the application. The domain part in a
hexagonal architecture.
* `spi`: contains ports on which the domain depends. This is the "left" part in a hexagonal architecture.
* `api`: contains ports that leverage the domain model. This is the "right" part in a hexagonal architecture.

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
mvn spring-boot:run -f app/pom.xml
```

Run frontend.
```
cd frontend/app
npm install
npm run dev
```

The application will be available on address http://localhost:3000/.