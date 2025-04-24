# petclinic FE

cd .\demo-spring-petclinic-angular\
docker build -t pet-fe . ; docker run -p 8080:8080 pet-fe 

find it in: http://localhost:8080/petclinic/

# petclinic BE

cd .\demo-spring-petclinic-java\
docker compose --profile postgres up --build

access the DB from: localhost:5433


the apis will be exposed from: http://localhost:8081/petclinic/api
