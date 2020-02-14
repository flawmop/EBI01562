# EBI01562 - Senior Java Software Developer

 REST API which stores, updates, retrieves and deletes Person entities

## Requirements

 1. Java 8
 1. Maven
 1. git
 1. jq (optional)

Tested in [Vagrant](https://github.com/joanmarcriera/vagrant-file-for-java-apps) (after (1) [issue](https://github.com/hashicorp/vagrant/issues/9442#issuecomment-363080565), and (2) (`vagrant up --provider virtualbox`).

## Install and start REST API

 1. Clone repository
```bash
 $ git clone https://github.com/flawmop/EBI01562.git
```
 2. Move to cloned and start
```bash
 $ cd EBI01562
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
 | POST | /persons | ADMIN | Add a new person |
 | PATCH | /persons/{personId} | ADMIN | Update a person (by id) |
 | DELETE | /persons/{personId} | ADMIN | Delete a person (by id) |

#### Post-startup

 [Person Controller](http://127.0.0.1:8080/swagger-ui.html#/person-controller)

## Example API Operations

```bash
curl -v -u user:password 127.0.0.1:8080/persons | jq
curl -v -u admin:password 127.0.0.1:8080/persons -H 'Content-type:application/json' -d '{"name": "fish1"}' | jq
curl -v -u admin:password 127.0.0.1:8080/persons -H 'Content-type:application/json' -d '{"name": "fish2"}' | jq
curl -v -u user:password 127.0.0.1:8080/persons/1 | jq
curl -v -u admin:password -X PATCH 127.0.0.1:8080/persons/1 -H 'Content-type:application/json' -d '{"name": "fish1.1"}' | jq
curl -v -u user:password 127.0.0.1:8080/persons | jq
curl -v -u admin:password -X DELETE 127.0.0.1:8080/persons/1 | jq
curl -v -u admin:password -X DELETE 127.0.0.1:8080/persons/2 | jq
curl -v -u user:password 127.0.0.1:8080/persons | jq
```