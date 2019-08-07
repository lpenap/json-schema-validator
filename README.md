# JSON Schema Validator for local & remote files
[![codebeat badge](https://codebeat.co/badges/02872181-0b92-4c56-b844-edc41724b6a8)](https://codebeat.co/projects/github-com-lpenap-json-schema-validator)
[![Build Status](https://travis-ci.org/lpenap/json-schema-validator.svg?branch=master)](https://travis-ci.org/lpenap/json-schema-validator)

Simple JSON validation facility using Draft v4, v6 and v7. This is oriented to be executed
as a part of a batch process, on a server or as part of integrated tests. It can be used to
test your API in development server or local files.

The application can be configured without modifying java code to accommodate basic
validation needs.

## Features
* Properties configuration with a queue to process several JSONs.
* Validator can fetch local files or make http/https requests.
* Remote API requests can be configured with custom headers.

## Requirements
* Maven 3 and a java 1.8 compatible compiler

## Quick Start
* Requires maven 3, and java 1.8
* Clone and execute examples:
```bash
git clone https://github.com/lpenap/json-schema-validator.git
mvn exec:java
```
After executing the bundled examples, you can see the following output in the console:
```bash
[main] INFO  c.p.json.validator.Launcher - Loading Validator...
[main] INFO  c.p.json.validator.JSONValidator - Validating 2  elements in queue...
[main] INFO  c.p.json.validator.JSONValidator - /example/com.example.api.json -> OK !
[main] INFO  c.p.json.validator.JSONValidator - Requesting remote Json: https://jsonplaceholder.typicode.com/users
[main] INFO  c.p.json.validator.JSONValidator - https://jsonplaceholder.typicode.com/users -> OK !
[main] INFO  c.p.json.validator.Launcher - Done.

```

## Build jar with dependencies
```bash
mvn install
```
The jar file will be located in `target/json-validator-0.X-jar-with-dependencies.jar`

## Configuration for local files and remote APIs
In order to validate each of your desired JSONs, you need to get a working
schema v4, v6 or v7 for each of your JSONs. You can use [JSONSchema.net](http://jsonschema.net/#/) to generate a schema from a valid Json.

Configuration can be made modifying `/src/main/resources/json-validator-properties.json`

Configuration example:
```JSON
{
  "version": 2,
  "queue": [
    {
      "is_remote": false,
      "is_array": false,
      "path": "/example/com.example.api.json",
      "schema": "/example/com.example.api-schema.json"
    },
    {
      "is_remote": true,
      "is_array": true,
      "path": "https://jsonplaceholder.typicode.com/users",
      "schema": "/example/com.typicode.jsonplaceholder-users.json",
      "method": "GET",
      "headers": [
        {
          "key": "Cache-Control",
          "value": "no-cache"
        },
        {
          "key": "Accept",
          "value": "application/json"
        }
      ]
    }
  ]
}
```

Each object specified in the `"queue"` can have the following keys:
* `is_remote`: If `true`, specifies a remote JSON or API or `false` for a file in the filesystem.
* `is_array`: If `true`, the main Json object will be treated as an array.
* `path`: Local filesystem path or URL (with http or https protocol).
* `schema`: Local path of the JSON schema to be used.
* `method` (optional): Method to be used when `is_remote` is `true` (defaults to GET).
* `headers` (optional): Custom headers to be included when invoking a remote API.

