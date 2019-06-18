# Terminal Rest API

This is a rest api for terminal data. This project was created as a challenge. 

The project was made using Java 8 and Spring Boot 2.1.5. The database used is 
an in-memory database called H2. The Java and Spring combination was choose for
this project for my professional experience with both an i also believe the two
are a great language and framework for API development.

## Pre-requisites

The only pre-requisite to run this project is to have the Java Runtime 
Environment (JRE) 1.8 or greater installed. 

## Installation

To simple execute the project, run this command on the project root:

`mvn spring-boot:run`

or, to generate a jar use:

`mvn -DskipTests install`

then run:

`java -jar /target/spring-rest-api-<version>.jar`

## Tests

To execute all project tests run:

`mvn test`

## JSON Schema

To be valid, a terminal has to be define as following json schema:

```json
{
    "title": "Terminal",
    "type": "object",
    "properties": {
        "logic": {
          "type": "integer"
        },
        "serial": {
          "type": "string"
        },
        "model": {
          "type": "string"
        },
        "sam": {
            "type": "integer",
            "minimum": 0
        },
        "ptid": {
          "type": "string"
        },
        "plat": {
          "type": "integer"
        },
        "version": {
          "type": "string"
        },
        "mxr": {
          "type": "integer"
        },
        "mxf": {
          "type": "integer"              
        },
        "verfm": {
          "type": "string"
        }
    },
    "required": ["logic", "serial", "model", "version"]
}
```

## Usage

Once the project is running, it will listen for http request at port 8080. 
There are 4 endpoints in the API:

* GET v1/terminal
    * This endpoint accepts 3 query parameters: page, size and sort. Page and 
    size controls the pagination where size is the total elements per page and
    page is the current page to find. Sort is actually a list where o can 
    specify fields to be sort and the sort direction. Any field in the schema can
    be used for sorting. This an example request:
        * http://localhost:8080/v1/terminal?page=1&size=5&sort=logic,desc&sort=version,asc  

* GET v1/terminal/{logic}
    * This endpoint retrieves a single terminal, as logic is a unique identifier
    of a terminal.

* POST v1/terminal
    * This endpoint accepts only content-type of text/html with the fields 
    separated by semicolon in the exactly order as the json schema presents. An 
    input example is: 
        * 44332211;123;PWWIN;0;F04A2E4088B;4;8.00b3;0;16777216;PWWIN
        
* PUT v1/terminal/{logic}
    * This endpoint, different from the post, accepts a json input following the
    json schema to update an existing terminal.

