package com.insilicosoft.api.person.entity;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.assertj.core.util.Preconditions;
import org.springframework.util.StringUtils;

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

  @Column(nullable = false)
  private String firstName;
  @Column(nullable = false)
  private String lastName;
  // Age is optional
  @Column
  private Integer age;
  // Favourite colour is optional
  @Column
  private String favouriteColour;
  // Hobbies are optional -- empty array indicates no hobbies (or not specified)
  @Column(nullable=false)
  private String[] hobbies = {};

  /* Default constructor */
  protected Person() {}

  /**
   * Initialising constructor.
   * 
   * @param firstName First name.
   * @param lastName Last name.
   * @param age Age (or {@code null}).
   * @param favouriteColour Favourite colour (or {@code null}).
   * @param hobbies Hobbies (or empty {@code String[]} if no hobbies). 
   * @throws IllegalArgumentException Invalid name components supplied. 
   * @throws NullPointerException {@code null} passed as {@link #hobbies}.
   */
  public Person(final String firstName, final String lastName,
                final Integer age, final String favouriteColour,
                final String[] hobbies) throws IllegalArgumentException,
                                               NullPointerException {
    if (StringUtils.isEmpty(firstName) || StringUtils.isEmpty(lastName)) {
      throw new IllegalArgumentException("First and last name must be provided");
    }

    setFirstName(firstName);
    setLastName(lastName);
    setAge(age);
    setFavouriteColour(favouriteColour);
    setHobbies(hobbies);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Person [id=" + id + ", firstName=" + firstName + ", lastName="
        + lastName + ", age=" + age + ", favouriteColour=" + favouriteColour
        + ", hobbies=" + Arrays.toString(hobbies) + "]";
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
   * Retrieve the first name.
   * 
   * @return Person's first name.
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Assign the first name.
   * 
   * @param firstName First name to assign.
   */
  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  /**
   * Retrieve the last name.
   * 
   * @return Person's last name.
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Assign the last name.
   * 
   * @param lastName Last name to assign.
   */
  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  /**
   * Retrieve the age.
   * 
   * @return The age (or {@code null} if not assigned).
   */
  public Integer getAge() {
    return age;
  }

  /**
   * Assign the age.
   * 
   * @param age Age to assign (or {@code null} if not supplied).
   */
  public void setAge(Integer age) {
    this.age = age;
  }

  /**
   * Retrieve the favourite colour.
   * 
   * @return Favourite colour (or {@code null} if not supplied).
   */
  public String getFavouriteColour() {
    return favouriteColour;
  }

  /**
   * Assign the favourite colour.
   * 
   * @param favouriteColour Favourite colour to assign (or {@code null} if no
   *                        favourite).
   */
  public void setFavouriteColour(final String favouriteColour) {
    this.favouriteColour = favouriteColour;
  }

  /**
   * Retrieve the hobbies.
   * 
   * @return Person's hobbies (or empty array if no hobbies / not supplied).
   */
  public String[] getHobbies() {
    return hobbies;
  }

  /**
   * Assign the hobbies.
   * <p>
   * Do not pass a {@code null} value. 
   * 
   * @param hobbies Hobbies to assign. Empty array if not hobbies or not
   *                specified.
   * @throws NullPointerException If argument is {@code null}.
   */
  public void setHobbies(final String[] hobbies)
                         throws NullPointerException {
    Preconditions.checkNotNull(hobbies);
    this.hobbies = hobbies;
  }
}