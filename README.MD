# Architecture and Implementation Details

This document captures the architecture decisions made while
developing this application, also it captures key points about
the API, storage, and possible future improvements.

The overall implementation architecture is depicted in the
following diagram - 

![Architecture](arch-design.png)

The main objective is to provide endpoints to provide a service for
storing, retrieving, updating, and deleting `Person` entity via
REST API. The person entity is represented using the following JSON
format - 

**NOTE**: An additional `id` field is included to be used as the
unique identifier for each `Person` entity since all other attributes
could potentially be same for two different person, and there is
no explicit constraint mentioned in the assignment.

```json
{
    "id": "602970c6340b7a394ba68c04",
    "first_name": "John",
    "last_name": "Keynes",
    "age": "29",
    "favourite_colour": "red"
  }
```
 
## REST API to query

There are a few REST APIs provided in the application to perform 
CRUD opeation for `Person` resource. They are accessible at - 
- `GET /person/{id}` - To query a specific person with Id.
- `GET /person` - To query all the persons available. The response is paginated.
- `POST /person` - To create/store a new person entity.
- `PUT /person/{id}` - To update an existing person entity by unique Id.
- `DELETE /person/{id}` - To delete an existing person entity by unique id.

The swagger document is accessible at  - http://localhost:8080/docs/index.html

## Security

Security is implemented using static username/password and predefined scopes. 
The overall idea was to use a proof-of-concept and provide a mock
implementation of a authorization service to generate JWT `access_token`.

Two pre-defined users are included - 

- user/user - use this for GET endpoints
- admin/admin - use this for all endpoints

A token endpoint is incorporated as following - 

- `POST /token` - call with API with following payload to get a JWT
access_token. Example - 

```json
{
  "username" : "admin",                                                                                                                                    
  "password" : "admin"
}
```

In CURL - 

```shell script
curl -X POST http://localhost:8080/token -H 'Content-Type: application/json' -d \ '{
"username" : "admin",                                                                                                                                    
"password" : "admin"
}'

```

This `access_token` could be used to call the `/person` endpoints.

Example in curl -

```shell script
curl http://localhost:8080/person/fdjdkjf -H 'Content-Type: application/json' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaXNzIjoiZWJpLWF1dGgtc2VydmljZSIsInNjb3BlIjpbImViaTp1c2VyIl0sImlhdCI6MTYxMzM4MDM2NSwiZXhwIjoxNjEzMzgwNTQ1fQ.-THwQGEuzrolOspxrq2FZ-SZjYgdkaeV5qIOBR9eHxU'
```  

## Data Store - MongoDB

The data store is loosely implemented using mongoDB, and could be replaced
with other potential data store as per requirement. MongoDB provides efficient
storage with the ability to shard and scale. 

For the testing and deployment of this application, a Docker based mongoDB 
service is used, and it is provided as a container in the `docker-compose.yml`

Also, a database called `ebi-person-service` is created with one collection - 
`persons`. The database and user/credential is created at startup of the
docker container(automatically executed).

## Dependencies Used

The programming language using is `Java 11` with `Spring Boot 2.4.2`

This application mostly relies on Spring framework, and includes
the `spring-boot-starter-**` dependencies. A few notable dependencies
used are related - `data-mongodb`, `web`. 

Also for Unit Testing, the `spring-boot-starter-test` module is used.
The `de.flapdoodle.embed.mongo` is used to run embedded mongoDB
server for testing.

## Execution Instructions

In order to execute this application, a `docker-compose.yml` file is
provided. This requires `docker-compose` to be present in the system.

Also, a `Makefile` is provided to capture all the build/run commands.

 - To build jar - `mvn clean install` or `make jar`

- To build and run all the services using docker-compose execute - 

    ```shell script
    docker-compose up --build
    ```
  or `make docker-compose-run`

## Future Improvement Considerations

Though, the implementation of this application is good for deployment and
serves the functionality with certain scales, the following future 
improvements could be considered - 

- **Integrate with Auth provide** (Authentication and Authorization) in the REST API. 
Since the implementation for security is only for proof-of-concept, this could
be further enhanced to use an external auth-provider like `Keycloak` or `Okta`.
- **Search and Filter** - The GET API provides the ability to find a `Person` 
with it's unique Id or get all the Person entity as a paginated response. 
However, for more interactive implementation, we could consider providing
a search and filter based API easily.