package com.ebi.app.store.impl;

import com.ebi.app.model.Person;
import com.ebi.app.repository.PersonRepository;
import com.ebi.app.store.PersonStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PersonStoreImpl implements PersonStore<Person> {

    private final PersonRepository personRepository;

    @Override
    public Optional<Person> findById(String id) {
        return personRepository.findById(id);
    }

    @Override
    public Person save(Person person) {
        return personRepository.insert(person);
    }

    @Override
    public Person update(Person person) {
        return personRepository.save(person);
    }

    @Override
    public void deleteById(String id) {
        personRepository.deleteById(id);
    }

    @Override
    public Page<Person> findAll(int page, int size) {
        var paging = PageRequest.of(page, size);
        return personRepository.findAll(paging);
    }
}
