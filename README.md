# EBI01562 - Senior Java Software Developer

 REST API which stores, updates, retrieves and deletes Person entities

## Requirements

 1. Java 8
 1. Maven
 1. git
 1. jq (optional)

Tested in [Vagrant](https://github.com/joanmarcriera/vagrant-file-for-java-apps) (after (1) [issue](https://github.com/hashicorp/vagrant/issues/9442#issuecomment-363080565), and (2) `vagrant up --provider virtualbox`).

## Install, test, and start REST API

 1. Clone repository
```bash
 $ git clone https://github.com/flawmop/EBI01562.git
```
 2. Move to cloned repository and start
```bash
 $ cd EBI01562
```
 3. Unit and integration testing
```bash
 $ mvn clean verify
```
 4. (Optional `sudo yum install lynx -y`) Code coverage
```bash
 $ lynx target/site/jacoco/index.html
```
 5. Start REST API.
```bash
 $ mvn clean spring-boot:run
```

## User credentials

 | Username | Password | ROLE(S) |
 | --- | --- | --- |
 | user | password | USER |
 | admin | password | USER, ADMIN |

## API documentation

#### Summary

 | Action | URL | ROLE | Operation |
 | --- | --- | --- | --- |
 | GET | /persons | USER | Retrieve all users |
 | GET | /persons/{personId} | USER | Retrieve a single user (by id) |
 | PATCH | /persons/{personId} | ADMIN | Modify a person's age (by id) |
 | POST | /persons | ADMIN | Add a new person |
 | PUT | /persons/{personId} | ADMIN | Update a person (by id) |
 | DELETE | /persons/{personId} | ADMIN | Delete a person (by id) |

#### Post-startup

 [Swagger 2](https://swagger.io/) [Person Controller](http://127.0.0.1:8080/swagger-ui.html#/person-controller)

## Example API Operations

```bash
curl -v -u user:password 127.0.0.1:8080/persons | jq
curl -v -u admin:password 127.0.0.1:8080/persons -H 'Content-type:application/json' \
        -d '{"first_name":"fn1","last_name":"ln1","age":10,"favourite_colour":"fc1","hobby":["h1.1", "h1.2"]}' | jq
curl -v -u admin:password 127.0.0.1:8080/persons -H 'Content-type:application/json' \
        -d '{"first_name":"fn2","last_name":"ln2","age":20,"favourite_colour":"fc2","hobby":["h2.1", "h2.2"]}' | jq
curl -v -u user:password 127.0.0.1:8080/persons | jq
curl -v -u user:password 127.0.0.1:8080/persons/1 | jq
curl -v -u admin:password -X PUT 127.0.0.1:8080/persons/1 -H 'Content-type:application/json' \
        -d '{"first_name":"fn1.1","last_name":"ln1.1","age":11,"favourite_colour":"fc1.1","hobby":["h1.11", "h1.21"]}' | jq
curl -v -u user:password 127.0.0.1:8080/persons/1 | jq
curl -v -u admin:password -X PATCH 127.0.0.1:8080/persons/1 -H 'Content-type:application/json' \
        -d '{"age":12}' | jq
curl -v -u user:password 127.0.0.1:8080/persons/1 | jq
curl -v -u admin:password -X DELETE 127.0.0.1:8080/persons/1 | jq
curl -v -u admin:password -X DELETE 127.0.0.1:8080/persons/2 | jq
curl -v -u user:password 127.0.0.1:8080/persons | jq
```

## TODO

```bash
grep -r 'TODO' src/ | awk -F'TODO' '{print $2}'
```