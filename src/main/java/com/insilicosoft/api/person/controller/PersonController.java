package com.insilicosoft.api.person.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insilicosoft.api.person.exception.InvalidRequestException;
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
  private class PersonDtoResource extends ResourceSupport {
    private String personId;
    private String personName;

    public PersonDtoResource(final PersonDto personDto) {
      super();
      setPersonId(String.valueOf(personDto.getId()));
      setPersonName(personDto.getName());
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
   *     "personDtoResourceList": [
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
  public Resources<PersonDtoResource> all() {
    log.debug("~all() : Invoked.");

    final List<PersonDtoResource> allPeople = new ArrayList<PersonDtoResource>();
    for (final PersonDto personDto : personService.all()) {
      final PersonDtoResource personDtoResource = new PersonDtoResource(personDto);
      final String personId = personDtoResource.getPersonId();
      final Link selfLink = linkTo(PersonController.class).slash(personId)
                                                          .withSelfRel();
      personDtoResource.add(selfLink);

      allPeople.add(personDtoResource);
    }

    return new Resources<PersonDtoResource>(allPeople,
                                            linkTo(PersonController.class).withSelfRel());
  }

  /**
   * Add a new person (via a {@code POST} request).
   * 
   * @param newPerson New person to add.
   * @return The new person.
   * @throws InvalidRequestException If invalid data supplied.
   */
  @PostMapping()
  public PersonDto newPerson(final @RequestBody
                                   PersonDto newPerson) 
                             throws InvalidRequestException {
    log.debug("~newPerson() : Invoked : '" + newPerson + "'");

    return personService.newPerson(newPerson);
  }
}