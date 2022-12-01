This is a simple spring boot project using spring security, mysql with swagger api documentation

First run the project:
$ mvn spring-boot:run

Swagger Urls:
http://localhost:9090/service-api/swagger-ui/
http://localhost:9090/service-api/v3/api-docs

Using the below link get the access and refresh token:
http://localhost:9090/service-api/api/auth/authenticate

Request:
username and password

Response: 
{
  "username": "admin@gmail.com",
  "accessToken": "accessToken",
  "refreshToken": "refreshToken",
  "authorities": [
    "ADMIN",
    "USER"
  ]
}

From swagger authorize tab click authorize copy the accessToken and insert into the value field
Bearer {{accessToken}}

By doing this the accessToken will be passed with the Authorization header 