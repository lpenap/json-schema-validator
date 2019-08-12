[![codebeat badge](https://codebeat.co/badges/b9cae666-0fb9-4562-9a40-36ab93a8c47f)](https://codebeat.co/projects/github-com-lpenap-json-schema-validator-master)
[![Build Status](https://travis-ci.org/lpenap/json-schema-validator.svg?branch=master)](https://travis-ci.org/lpenap/json-schema-validator)
[![GitHub release](https://img.shields.io/github/release/lpenap/json-schema-validator)](/lpenap/json-schema-validator/releases/latest)

# JSON Schema Validator for local & remote files
Simple JSON validation facility using Draft v4, v6 and v7. This is oriented to be executed
as a part of a batch process, on a server or as part of integrated tests. It can be used to
test your API in development server or local files.

## Features
* Preferences configuration with a queue to process several JSONs.
* Validator can fetch local files or make http/https requests.
* Remote API requests can be configured with custom headers.
* Errors reported in output:
  * Not found / IO Error.
  * Invalid Json (i.e. malformed json).
  * Invalid against schema.

## Requirements
* Maven 3 (for development only) 
* java 1.8 compatible compiler to run.

## Quick Start
### From Sourcecode
* Clone and execute bundled examples:
```bash
git clone https://github.com/lpenap/json-schema-validator.git
mvn exec:java
```
### From jar file
* Download [latest release](/lpenap/json-schema-validator/releases/latest) and execute
```bash
java -jar json-validator-0.X-jar-with-dependencies.jar
```

After executing the bundled examples, you can see the following output in the console:
```bash
 INFO  c.p.json.validator.CliLauncher - Starting json-schema-validator
 INFO  c.p.json.validator.CliLauncher -   Preferences file was not specified, running bundled examples.
 INFO  c.p.json.validator.JSONValidator -   Validating 2 elements in queue...
 INFO  c.p.json.validator.JSONValidator -   /example/com.example.api.json -> [OK] !
 INFO  c.p.json.validator.JSONValidator -   https://jsonplaceholder.typicode.com/users -> [OK] !
 INFO  c.p.json.validator.CliLauncher - Done json-schema-validator
```
## Configuringation
In order to validate each of your desired JSONs, you need to get a working
schema v4, v6 or v7 for each of your JSONs. You can use [JSONSchema.net](http://jsonschema.net/#/) to generate a schema from a valid Json.

Example:
```json
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
Queue objects:
* `is_remote`: `true` for specifies a remote JSON/API. `false` for a file in the filesystem.
* `is_array`: If `true`, the root Json object present in `path` will be treated as an array.
* `path`: Local filesystem path or URL (include the protocol, i.e. http, https).
* `schema`: Local path of the JSON schema to be used.
* `method` (optional): Method to be used when `is_remote` is `true` (defaults to GET).
* `headers` (optional): Custom headers to be included when invoking a remote API.
### From Sourcecode
```bash
mvn exec:java -Dexec.arguments="path/to/preferences.json"
```
### From jar file
```bash
java -jar json-validator-0.X-jar-with-dependencies.jar "path/to/preferences.json"
```
## Build jar with dependencies
```bash
mvn install
```
The jar file will be located in `target/json-validator-0.X-jar-with-dependencies.jar`
