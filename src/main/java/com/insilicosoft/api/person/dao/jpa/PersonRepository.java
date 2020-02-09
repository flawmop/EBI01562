package com.insilicosoft.api.person.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insilicosoft.api.person.entity.Person;

/**
 * Person repository.
 * 
 *
 * @author geoff
 * @see https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
  
}