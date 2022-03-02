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
- go to localhost:8080/v1/registration


## Admin (to manually query db)

- to find the mysql container id, whose image name is mysql:latest
```bash
docker ps -a 
```
- to access mysql container 

```bash 
docker exec -it db_container_id /bin/sh
```

- Once in the (mysql) container, password is 'root'
```bash
mysql -u root -p  
```

## API documentation 
- [docs](localhost:8080/apidocs/)

## Test
- install [postman](https://www.postman.com/downloads/)

- follow the [docs](https://learning.postman.com/docs/getting-started/introduction/)

## TODO
implement better error/exception handling