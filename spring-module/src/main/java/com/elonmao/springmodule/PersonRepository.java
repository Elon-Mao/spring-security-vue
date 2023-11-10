package com.elonmao.springmodule;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PersonRepository extends PagingAndSortingRepository<Person, String>, CrudRepository<Person, String> {

}
