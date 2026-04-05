# CookAlchemy

## How to run this app as developer


Run developer environment services.
```
cd deven
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

Application will be available on address http://localhost:3000/.