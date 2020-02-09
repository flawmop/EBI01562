package com.insilicosoft.api.person.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * System/Entity representation of a Person.
 *
 * @author geoff
 */
@Entity
public class Person {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  /* Default constructor */
  protected Person() {}

  /**
   * Initialising constructor.
   * 
   * @param name Person's name.
   * @throws IllegalArgumentException On assigning an empty {@linkplain #name}. 
   */
  public Person(final String name) throws IllegalArgumentException {
    if (name == null || name.trim().length() == 0) {
      // TODO: Check throwing exceptions from constructors.
      throw new IllegalArgumentException("Invalid null/empty name encountered");
    }

    setName(name);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Person [id=" + id + ", name=" + name + "]";
  }

  /**
   * Retrieve system id for person.
   * 
   * @return Person's system identifier, or {@code null} if unassigned.
   */
  public Long getId() {
    return id;
  }

  /**
   * Retrieve person's name.
   * 
   * @return Person's name, or {@code null} if unassigned.
   */
  public String getName() {
    return name;
  }

  /**
   * Assign person's name.
   * 
   * @param name Name to assign.
   */
  public void setName(final String name) {
    this.name = name;
  }
}