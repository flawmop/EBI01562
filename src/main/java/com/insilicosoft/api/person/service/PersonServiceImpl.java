package com.insilicosoft.api.person.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.insilicosoft.api.person.dao.jpa.PersonRepository;
import com.insilicosoft.api.person.entity.Person;
import com.insilicosoft.api.person.exception.InvalidRequestException;
import com.insilicosoft.api.person.exception.PersonNotFoundException;
import com.insilicosoft.api.person.value.PersonDto;

/**
 * Implementation of the person service.
 *
 * @author geoff
 */
@Component
public class PersonServiceImpl implements PersonService {

  @Autowired
  private PersonRepository repository;

  private static final Log log = LogFactory.getLog(PersonServiceImpl.class);

  /* (non-Javadoc)
   * @see com.insilicosoft.api.person.service.PersonService#all()
   */
  public List<PersonDto> all() {
    log.debug("~all() : Invoked.");

    final List<PersonDto> everybody = new ArrayList<PersonDto>();

    // Transfer internal representation to public-facing representation.
    for (final Person eachPerson: repository.findAll()) {
      everybody.add(new PersonDto(eachPerson));
    }

    return everybody;
  }

  /* (non-Javadoc)
   * @see com.insilicosoft.api.person.service.PersonService#deletePerson(java.lang.Long)
   */
  public void deletePerson(final Long personId) throws PersonNotFoundException {
    log.debug("~deletePerson() : Invoked : [" + personId + "]");

    try {
      repository.deleteById(personId);
    } catch (EmptyResultDataAccessException e) {
      throw new PersonNotFoundException(String.valueOf(personId));
    }
  }

  /* (non-Javadoc)
   * @see com.insilicosoft.api.person.service.PersonService#newPerson(com.insilicosoft.api.person.value.PersonDto)
   */
  public PersonDto newPerson(final PersonDto newPerson)
                             throws InvalidRequestException {
    log.debug("~newPerson() : Invoked : '" + newPerson + "'");

    Person personEntity = null;
    try {
      // Transfer public-facing representation to internal representation
      personEntity = new Person(newPerson.getName());
    } catch (IllegalArgumentException e) {
      throw new InvalidRequestException(e.getMessage());
    }

    return new PersonDto(repository.save(personEntity));
  }

  /* (non-Javadoc)
   * @see com.insilicosoft.api.person.service.PersonService#one(java.lang.Long)
   */
  @Override
  public PersonDto one(final Long personId) throws PersonNotFoundException {
    log.debug("~one() : Invoked : [" + personId + "]");

    final Person foundPerson = repository.findById(personId)
                                         .orElseThrow(
                                             () -> new PersonNotFoundException(String.valueOf(personId))
                                           );
    return new PersonDto(foundPerson);
  }

  /* (non-Javadoc)
   * @see com.insilicosoft.api.person.service.PersonService#updatePerson(java.lang.Long, com.insilicosoft.api.person.value.PersonDto)
   */
  public PersonDto updatePerson(final Long personId, final PersonDto personDto)
                                throws InvalidRequestException,
                                       PersonNotFoundException {
    log.debug("~updatePerson() : Invoked : [" + personId + "] : '" + personDto + "'");
    final Person updatedPerson = repository.findById(personId)
                                           .map(existingPerson -> {
                                             existingPerson.setName(personDto.getName());
                                             return repository.save(existingPerson);
                                           }).orElseThrow(
                                             () -> new PersonNotFoundException(String.valueOf(personId))
                                           );
    return new PersonDto(updatedPerson);
  }

}