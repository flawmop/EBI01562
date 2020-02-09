package com.insilicosoft.api.person.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public class PersonController {

  @Autowired
  private PersonService personService;

  private static final Log log = LogFactory.getLog(PersonController.class);

  /**
   * Retrieve a collection of all people.
   * 
   * @return Collection of all people.
   */
  @GetMapping("/persons")
  public List<PersonDto> all() {
    log.debug("~all() : Invoked.");

    return personService.all();
  }

  /**
   * Add a new person.
   * 
   * @param newPerson New person to add.
   * @return The new person.
   * @throws InvalidRequestException If invalid data supplied.
   */
  @PostMapping("/persons")
  public PersonDto newPerson(final @RequestBody
                                   PersonDto newPerson) 
                             throws InvalidRequestException {
    log.debug("~newPerson() : Invoked : '" + newPerson + "'");

    return personService.newPerson(newPerson);
  }
}