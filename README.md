http://localhost:9090/service-api/v3/api-docs



Spring Security and JWT simple application

First run the project:
$ mvn spring-boot:run

Then,
Using PostMan
Step 1.

Url: http://localhost:8080/authenticate
Header:
Key: Content-Type
Value: application/json
Body:
{

	"username": "foo",
	"password": "foo"
}

Response:
{
    "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb28iLCJleHAiOjE1ODA5MjQ5MzMsImlhdCI6MTU4MDg4ODkzM30.Zl6GZsI0VSSRcnwK2L8xNAiwqCL2u2s14FwJWOl59ak"
}

Step 2.
Url: http://localhost:8080/hello
Header:
Key: Content-Type
Value: application/json

Key: Authorization
Value: Bearer from step 1 jwt token value
Body:
{

        "username": "foo",
        "password": "foo"
}

Response: Hello World

