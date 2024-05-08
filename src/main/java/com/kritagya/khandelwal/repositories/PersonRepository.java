package com.kritagya.khandelwal.repositories;

import com.kritagya.khandelwal.models.Person;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends ReactiveMongoRepository<Person,String> {

}
