package com.insilicosoft.api.person.service;

import java.util.List;

import com.insilicosoft.api.person.exception.InvalidRequestException;
import com.insilicosoft.api.person.value.PersonDto;

/**
 * Interface to person ops.
 *
 * @author geoff
 */
public interface PersonService {

  /**
   * Retrieve all people.
   * 
   * @return Collection of all people. Empty if none available.
   */
  List<PersonDto> all();

  /**
   * Add a new person.
   * 
   * @param newPerson Person to add.
   * @return Added person.
   * @throws InvalidRequestException If invalid data submitted.
   */
  PersonDto newPerson(final PersonDto newPerson) throws InvalidRequestException;

}