# JSON Schema Validator for local & remote files
[![codebeat badge](https://codebeat.co/badges/02872181-0b92-4c56-b844-edc41724b6a8)](https://codebeat.co/projects/github-com-lpenap-json-schema-validator)
[![Build Status](https://travis-ci.org/lpenap/json-schema-validator.svg?branch=master)](https://travis-ci.org/lpenap/json-schema-validator)

JSON validation using Draft v4, v6 and v7. This is a simple facility oriented to be executed
as a part of a batch process, on a server or as part of integrated tests. It can be used to
test your API in development or local files.

The application can be configured without modifying java code to accommodate basic
validation needs.

## Features
* Properties configuration with a queue to process several JSONs.
* Validator can fetch local files or make http/https requests.
* Remote API requests can be configured with custom headers.

## Install
* Requires maven 3, and java 1.8
* Clone this repo
```bash
git clone https://github.com/lpenap/json-schema-validator.git
```
* Execute it to verify proper working examples
```bash
mvn exec:java
```
* Generate simple jar or jar-with-dependencies
```bash
mvn install
```
* Execute jar
```bash
java -jar target/validator-0.1-jar-with-dependencies.jar
```

### Output example
After executing the bundled examples, you can see the following output in the console:
```
###############################################################################
Loading Validator...
Running validator...
# checking /example/com.example.api.json
  -> valid!
# checking https://private-2db5f-luisaugustopenapereira.apiary-mock.com/notes
  -> valid!
Done.
###############################################################################
```

* You can also import this project in eclipse to extend it.

## Configuration for local files and remote APIs
In order to validate each of your desired JSONs, you need to get a working
schema v4 for each of your JSONs. You can use [JSONSchema.net](http://jsonschema.net/#/).

The configuration `/src/main/resources/json-validator-properties.json` will be validated against its own schema at execution causing it to abort if a problem is found.

Configuration example:
```JSON
{
	"version": 1,
	"queue": [
		{
			"is_remote": false,
			"path": "/example/com.example.api.json",
			"schema": "/example/com.example.api-schema.json"
		},
		{
			"is_remote": true,
			"path": "https://private-2db5f-luisaugustopenapereira.apiary-mock.com/notes",
			"schema": "/example/com.apiary-mock-notes-schema.json",
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
* `path`: Local filesystem path or URL (with http or https protocol).
* `schema`: Local path of the JSON schema v4 to be used.
* `method` (optional): Method to be used when `is_remote` is `true` (defaults to GET).
* `headers` (optional): Custom headers to be included when invoking a remote API.

## Extending
Eclipse is recommended, you can clone and import the project into your workspace.
