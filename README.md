# Registration service API V1 (Metadata test)

### Run 

```bash
docker-compose up --force-recreate --build -d
```
- you may omit the '-d' to watch the logs
- For non docker setting
```bash
mvn spring-boot:run
```

- debug mode 
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--logging.level.org.springframework=TRACE
```

- specify build/run environment (dev, prod or staging...etc)

```bash
mvn spring-boot:run -Dspring.profiles.active=dev 
```

- go to 


## Admin (to manually query db)

- to find the mysql container id, whose image name is mysql:latest
```bash
docker ps -a 
```
- to access mysql container 

```bash 
docker exec -it db_container_id /bin/sh
```

- Once in the (mysql) container, for credentials see: .env file 
- adjust to your liking for env to env
```bash
mysql -u root -p  
```

## API documentation 
- [docs](http://localhost:8080/apidocs/)

## Test
- install [postman](https://www.postman.com/downloads/)

- follow the [docs](https://learning.postman.com/docs/getting-started/introduction/)

## TODO
implement better error/exception handling