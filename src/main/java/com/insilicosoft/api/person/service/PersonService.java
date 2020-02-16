package com.insilicosoft.api.person.service;

import java.util.List;

import com.insilicosoft.api.person.exception.InvalidRequestException;
import com.insilicosoft.api.person.exception.PersonNotFoundException;
import com.insilicosoft.api.person.value.AgeDto;
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
   * Delete a person.
   * 
   * @param personId System id of {@linkplain com.insilicosoft.api.person.entity.Person}
   *                 to delete.
   * @throws PersonNotFoundException If {@code Person} with specified
   *                                 {@code personId} not found. 
   */
  void deletePerson(final Long personId) throws PersonNotFoundException;

  /**
   * Add a new {@code Person}.
   * 
   * @param newPerson {@code Person} as DTO to add.
   * @return Added {@code Person} as DTO.
   * @throws InvalidRequestException If invalid data submitted.
   */
  PersonDto newPerson(final PersonDto newPerson) throws InvalidRequestException;

  /**
   * Retrieve {@code Person} with specified id.
   * 
   * @param personId System identifier.
   * @return Person with specified identifier.
   * @throws PersonNotFoundException If {@code Person} with specified
   *                                 {@code personId} not found. 
   */
  PersonDto one(final Long personId) throws PersonNotFoundException;

  /**
   * Update a {@code Person}.
   * 
   * @param personId System id of {@linkplain com.insilicosoft.api.person.entity.Person}
   *                 to delete.
   * @param personDto {@code Person} as DTO to update.
   * @return Updated {@code Person} as DTO.
   * @throws InvalidRequestException If invalid data submitted.
   * @throws PersonNotFoundException If {@code Person} with specified {@code id}
   *                                 not found. 
   */
  PersonDto updatePerson(final Long personId,
                         final PersonDto personDto)
                         throws InvalidRequestException, PersonNotFoundException;

  /**
   * Update the {@code Person}'s age.
   * 
   * @param personId System id of {@linkplain com.insilicosoft.api.person.entity.Person}
   *                 to delete.
   * @param personDto {@code Person} as DTO to update.
   * @return Updated {@code Person} as DTO.
   * @throws InvalidRequestException If invalid data submitted.
   * @throws PersonNotFoundException If {@code Person} with specified {@code id}
   *                                 not found. 
   */
  PersonDto updatePersonAge(final Long personId,
                            final AgeDto ageDto)
                            throws InvalidRequestException,
                                   PersonNotFoundException;

}