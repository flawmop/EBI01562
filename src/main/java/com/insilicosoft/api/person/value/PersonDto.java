package com.insilicosoft.api.person.value;

import com.insilicosoft.api.person.entity.Person;

/**
 * Public-facing <acronym title="Data Transfer Object">DTO</acronym>
 * representation for {@link Person} entities.
 *
 * @author geoff
 */
public class PersonDto {

  private Long id;
  private String name;

  /**
   * Initialising constructor.
   * 
   * @param id System identifier.
   * @param name Person's name.
   */
  public PersonDto(final Long id, final String name) {
    setId(id);
    setName(name);
  }

  /**
   * Initialising constructor from domain object.
   * 
   * @param person Domain object representation.
   */
  public PersonDto(final Person person) {
    this(person.getId(), person.getName());
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "PersonDto [id=" + id + ", name=" + name + "]";
  }

  /**
   * Retrieve the application identifier.
   * 
   * @return Application identifier (or {@code null} if unassigned).
   */
  public Long getId() {
    return id;
  }

  /**
   * Assign the application identifier.
   * 
   * @param id Application identifier to set
   */
  public void setId(final Long id) {
    this.id = id;
  }

  /**
   * Retrieve the name of the person.
   * 
   * @return Person's name (or {@code null} if unassigned).
   */
  public String getName() {
    return name;
  }

  /**
   * Assign the name of the person.
   * 
   * @param name Name to assign.
   */
  public void setName(final String name) {
    this.name = name;
  }

}