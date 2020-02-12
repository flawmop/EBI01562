package com.insilicosoft.api.person.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.hateoas.core.Relation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.insilicosoft.api.person.exception.InvalidRequestException;
import com.insilicosoft.api.person.exception.PersonNotFoundException;
import com.insilicosoft.api.person.service.PersonService;
import com.insilicosoft.api.person.value.PersonDto;

/**
 * Person Controller.
 *
 * @author geoff
 */
@RestController
@RequestMapping(value = "/persons")
@EnableHypermediaSupport(type = HypermediaType.HAL)
public class PersonController {

  @Autowired
  private PersonService personService;

  private static final Log log = LogFactory.getLog(PersonController.class);

  // Enable a HAL-compatible representation of the Person DTO!
  // https://stackoverflow.com/questions/33289753/how-to-change-the-property-name-of-an-embbed-collection-in-spring-hateos
  @Relation(collectionRelation = "persons")
  private class PersonDtoResource extends ResourceSupport {
    private String personId;
    private String personName;

    public PersonDtoResource(final PersonDto personDto) {
      super();
      setPersonId(String.valueOf(personDto.getId()));
      setPersonName(personDto.getName());

      final List<Link> links = new ArrayList<Link>();
      links.add(linkTo(PersonController.class).slash(getPersonId())
                                              .withSelfRel());
      try {
        // Plenty code here for sparse return! 
        links.add(linkTo(methodOn(PersonController.class).deletePerson(Long.valueOf(getPersonId())))
                                                         .withRel("delete"));
      } catch (PersonNotFoundException e) {}
      try {
        // Plenty code here for sparse return! 
        links.add(linkTo(methodOn(PersonController.class).updatePerson(Long.valueOf(getPersonId()), personDto))
                                                         .withRel("update"));
      } catch (InvalidRequestException e1) {
      } catch (PersonNotFoundException e2) {}

      for (final Link link : links) {
        add(link);
      }
    }

    public String getPersonId() {
      return personId;
    }

    public void setPersonId(String personId) {
      this.personId = personId;
    }

    @SuppressWarnings("unused")
    public String getPersonName() {
      return personName;
    }

    public void setPersonName(String personName) {
      this.personName = personName;
    }
  }

  /**
   * Retrieve a HAL-format collection of all people (via a {@code GET} request).
   * <pre>
   * {@code 
   * {
   *   "_embedded": {
   *     "persons": [
   *       {
   *         "personId": "1",
   *         "personName": "fish1",
   *         "_links": {
   *           "self": {
   *             "href": "http://127.0.0.1:8080/persons/1"
   *           }
   *         }
   *       },
   *       ..
   *   }
   * }
   * </pre>
   * @return Collection of all people.
   */
  @GetMapping(produces = { "application/hal+json" })
  @ResponseStatus(HttpStatus.OK)
  public Resources<PersonDtoResource> all() {
    log.debug("~all() : Invoked.");

    final List<PersonDtoResource> allPersons = new ArrayList<PersonDtoResource>();
    for (final PersonDto personDto : personService.all()) {
      final PersonDtoResource personDtoResource = new PersonDtoResource(personDto);

      allPersons.add(personDtoResource);
    }

    return new Resources<PersonDtoResource>(allPersons,
                                            linkTo(PersonController.class)
                                                  .withSelfRel());
  }

  /**
   * Delete a person (via a {@code DELETE} request).
   * 
   * @param personId System identifier of {@link Person} to delete.
   * @throws PersonNotFoundException If person not found.
   */
  @DeleteMapping(value = "/{personId}", produces = { "application/hal+json" })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> deletePerson(final @PathVariable("personId")
                                                 Long personId)
                                           throws PersonNotFoundException {
    log.debug("~deletePerson() : [" + personId + "]");

    personService.deletePerson(personId);

    return ResponseEntity.noContent().build();
  }

  /**
   * Add a new person (via a {@code POST} request).
   * 
   * @param newPerson New person to add.
   * @return The new person.
   * @throws InvalidRequestException If invalid data supplied.
   */
  @PostMapping(produces = { "application/hal+json" })
  @ResponseStatus(HttpStatus.CREATED)
  public Resources<PersonDtoResource> newPerson(final @RequestBody
                                                      PersonDto personDto) 
                             throws InvalidRequestException {
    log.debug("~newPerson() : Invoked : '" + personDto + "'");

    final List<PersonDtoResource> colOfOne = new ArrayList<PersonDtoResource>();
    colOfOne.add(new PersonDtoResource(personService.newPerson(personDto)));
    return new Resources<PersonDtoResource>(colOfOne,
                                            linkTo(PersonController.class)
                                                  .withSelfRel());
  }

  /**
   * Update a person.
   * 
   * @param personId System identifier of {@link Person} to update.
   * @param personDto Requested update data.
   * @return The updated person.
   * @throws InvalidRequestException If invalid data supplied.
   * @throws PersonNotFoundException If person not found.
   */
  @PutMapping(value = "/{personId}", produces = { "application/hal+json" })
  @ResponseStatus(HttpStatus.OK)
  public Resources<PersonDtoResource> updatePerson(final @PathVariable("personId")
                                                         Long personId,
                                                   final @RequestBody
                                                         PersonDto personDto)
                                                   throws InvalidRequestException,
                                                          PersonNotFoundException {
    log.debug("~updatePerson() : Invoked : [" + personId + "] : '" + personDto + "'");

    final List<PersonDtoResource> colOfOne = new ArrayList<PersonDtoResource>();
    colOfOne.add(new PersonDtoResource(personService.updatePerson(personId,
                                                                  personDto)));
    return new Resources<PersonDtoResource>(colOfOne,
                                            linkTo(PersonController.class)
                                                  .withSelfRel());
  }
}