# JSON Schema Validator for local & remote files

JSON validation using schemas v4. This is a simple facility oriented to be executed
as a part of a batch process, on a server or as part of integrated tests. It can be used to
test your API in development or local files.

The application can be configured without modifying java code to accommodate basic
validation needs.

## Feautes
* Properties configuration with a queue to process several JSONs.
* Validator can fetch local files or make http/https requests.
* Remote API requests can be configured with custom headers.

## Installation
* clone this repo
```bash
git clone https://github.com/lpenap/json-schema-validator.git
```
* execute it to verify proper working examples
```bash
mvn exec:java
```