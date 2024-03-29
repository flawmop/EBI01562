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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

import com.insilicosoft.api.person.exception.InvalidRequestException;
import com.insilicosoft.api.person.exception.PersonNotFoundException;
import com.insilicosoft.api.person.service.PersonService;
import com.insilicosoft.api.person.value.AgeDto;
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

  /*
   *  Enable a HAL-compatible representation of the Person DTO!
   *  TODO: Consider https://spring.io/guides/tutorials/bookmarks/#_simplifying_link_creation
   */
  // https://stackoverflow.com/questions/33289753/how-to-change-the-property-name-of-an-embbed-collection-in-spring-hateos
  @Relation(collectionRelation = "persons")
  private class PersonResource extends ResourceSupport {
    private String personFirstName;
    private String personLastName;
    private Integer personAge;
    private String personFavouriteColour;
    private String[] personHobbies = {};

    public PersonResource(final PersonDto personDto) {
      super();

      final Long personId = personDto.getId();
      setPersonFirstName(personDto.getFirst_name());
      setPersonLastName(personDto.getLast_name());
      setPersonAge(personDto.getAge());
      setPersonFavouriteColour(personDto.getFavourite_colour());
      setPersonHobbies(personDto.getHobby());

      if (personId != null) {
        /*
         * Person has been persisted, so establish links to self, deletion and
         * updating operations.
         */
        final List<Link> links = new ArrayList<Link>();

        links.add(linkTo(PersonController.class).slash(personId)
                                                .withSelfRel());
        try {
          links.add(linkTo(methodOn(PersonController.class).deletePerson(personId))
                                                           .withRel("delete"));
        } catch (PersonNotFoundException e) {}
        try {
          links.add(linkTo(methodOn(PersonController.class).updatePerson(personId,
                                                                         personDto))
                                                           .withRel("update"));
        } catch (InvalidRequestException e1) {
        } catch (PersonNotFoundException e2) {}

        for (final Link link : links) {
          add(link);
        }
      }
    }

    @SuppressWarnings("unused")
    public String getPersonFirstName() {
      return personFirstName;
    }

    public void setPersonFirstName(final String personFirstName) {
      this.personFirstName = personFirstName;
    }

    @SuppressWarnings("unused")
    public String getPersonLastName() {
      return personLastName;
    }

    public void setPersonLastName(final String personLastName) {
      this.personLastName = personLastName;
    }

    @SuppressWarnings("unused")
    public Integer getPersonAge() {
      return personAge;
    }

    public void setPersonAge(final Integer personAge) {
      this.personAge = personAge;
    }

    @SuppressWarnings("unused")
    public String getPersonFavouriteColour() {
      return personFavouriteColour;
    }

    public void setPersonFavouriteColour(final String personFavouriteColour) {
      this.personFavouriteColour = personFavouriteColour;
    }

    @SuppressWarnings("unused")
    public String[] getPersonHobbies() {
      return personHobbies;
    }

    public void setPersonHobbies(final String[] personHobbies) {
      this.personHobbies = personHobbies;
    }
  }

  /**
   * Retrieve a HAL-format response of collection of all people (via a
   * {@code GET} request) e.g.
   * <pre>
   * {@code 
   * {
   *   "_embedded": {
   *     "persons": [
   *       {
   *         "personFirstName": "fish1chip1",
   *         "personLastName": "aslfk",
   *         "personAge": null,
   *         "personFavouriteColour": null,
   *         "personHobbies": [
   *           "fishing"
   *         ],
   *         "_links": {
   *           "self": {
   *           "href": "http://127.0.0.1:8080/persons/2"
   *           },
   *           "delete": {
   *             "href": "http://127.0.0.1:8080/persons/2"
   *           },
   *           "update": {
   *             "href": "http://127.0.0.1:8080/persons/2"
   *           }
   *         }
   *       },
   *       ..
   *   }
   * }
   * </pre>
   * @return Collection of all people (or {@link Problem} if invalid accept type)
   */
  @GetMapping(produces = { "application/hal+json", "application/problem+json" })
  @ResponseStatus(HttpStatus.OK)
  public Resources<PersonResource> all() {
    log.debug("~all() : Invoked.");

    final List<PersonResource> allPersons = new ArrayList<PersonResource>();
    for (final PersonDto personDto : personService.all()) {
      final PersonResource personDtoResource = new PersonResource(personDto);

      allPersons.add(personDtoResource);
    }

    return new Resources<PersonResource>(allPersons,
                                         linkTo(PersonController.class)
                                               .withSelfRel());
  }

  /**
   * Delete a person (via a {@code DELETE} request).
   * 
   * @param personId System identifier of {@link Person} to delete.
   * @throws PersonNotFoundException If person not found.
   */
  @DeleteMapping(value = "/{personId}",
                 produces = { "application/problem+json" })
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
  @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
               produces = { "application/hal+json", "application/problem+json" })
  @ResponseStatus(HttpStatus.CREATED)
  public Resources<PersonResource> newPerson(final @RequestBody
                                                   PersonDto personDto) 
                                             throws InvalidRequestException {
    log.debug("~newPerson() : Invoked : '" + personDto + "'");

    final List<PersonResource> colOfOne = new ArrayList<PersonResource>();
    colOfOne.add(new PersonResource(personService.newPerson(personDto)));
    return new Resources<PersonResource>(colOfOne,
                                         linkTo(PersonController.class)
                                               .withSelfRel());
  }

  /**
   * Retrieve specified {@link Person}.
   * 
   * @param personId System identifier of {@link Person} to delete.
   * @return The specified {@link Person}.
   * @throws PersonNotFoundException If person not found.
   */
  @GetMapping(value = "/{personId}",
              produces = { "application/hal+json", "application/problem+json" })
  @ResponseStatus(HttpStatus.OK)
  public Resources<PersonResource> one(final @PathVariable("personId")
                                             Long personId)
                                       throws PersonNotFoundException {
    log.debug("~one() : Invoked : [" + personId + "]");

    final List<PersonResource> colOfOne = new ArrayList<PersonResource>();
    colOfOne.add(new PersonResource(personService.one(personId)));
    return new Resources<PersonResource>(colOfOne,
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
  @PutMapping(value = "/{personId}",
              consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
              produces = { "application/hal+json", "application/problem+json" })
  @ResponseStatus(HttpStatus.OK)
  public Resources<PersonResource> updatePerson(final @PathVariable("personId")
                                                      Long personId,
                                                final @RequestBody
                                                      PersonDto personDto)
                                                throws InvalidRequestException,
                                                       PersonNotFoundException {
    log.debug("~updatePerson() : Invoked : [" + personId + "] : '" + personDto + "'");

    final List<PersonResource> colOfOne = new ArrayList<PersonResource>();
    colOfOne.add(new PersonResource(personService.updatePerson(personId,
                                                               personDto)));
    return new Resources<PersonResource>(colOfOne,
                                         linkTo(PersonController.class)
                                               .withSelfRel());
  }

  @PatchMapping(value = "/{personId}",
                consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                produces = { "application/hal+json", "application/problem+json" })
  @ResponseStatus(HttpStatus.OK)
  public Resources<PersonResource> updatePersonAge(final @PathVariable("personId")
                                                         Long personId,
                                                   final @RequestBody
                                                         AgeDto ageDto)
                                                   throws InvalidRequestException,
                                                          PersonNotFoundException {
    log.debug("~updatePersonAge() : Invoked : [" + personId + "] : '" + ageDto + "'");

    final List<PersonResource> colOfOne = new ArrayList<PersonResource>();
    colOfOne.add(new PersonResource(personService.updatePersonAge(personId,
                                                                  ageDto)));
    return new Resources<PersonResource>(colOfOne,
                                         linkTo(PersonController.class)
                                               .withSelfRel());
  }

}