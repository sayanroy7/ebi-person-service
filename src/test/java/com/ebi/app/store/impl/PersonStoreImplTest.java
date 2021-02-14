package com.ebi.app.store.impl;

import com.ebi.app.TestDataUtils;
import com.ebi.app.model.Person;
import com.ebi.app.repository.PersonRepository;
import com.ebi.app.store.PersonStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig
@DataMongoTest
class PersonStoreImplTest {

    private static ObjectMapper objectMapper;

    @Autowired
    private PersonRepository personRepository;

    private PersonStore<Person> personStore;

    private List<Person> personList;

    @BeforeAll
    public static void init() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        if (personStore == null) {
            personStore = new PersonStoreImpl(personRepository);
        }
        personList = personRepository.insert(TestDataUtils.getPersons(objectMapper));
    }

    @Test
    void findById() {
        var person = personList.get(0);
        var personEntity = personStore.findById(person.getId());
        assertNotNull(personEntity);
        assertTrue(personEntity.isPresent());
        assertEquals(person, personEntity.get());
    }

    @Test
    void save() {
        var personEntity = Person.builder().age(30)
                .firstName("John").lastName("Ringo")
                .favouriteColour("Red").build();
        var savedEntity = personStore.save(personEntity);
        assertNotNull(savedEntity);
        assertFalse(savedEntity.getId().isBlank());
        assertEquals(personEntity.getFirstName(), savedEntity.getFirstName());
        assertEquals(personEntity.getLastName(), savedEntity.getLastName());
        assertEquals(personEntity.getAge(), savedEntity.getAge());
        assertEquals(personEntity.getFavouriteColour(), savedEntity.getFavouriteColour());
    }

    @Test
    void update() {
        var personEntity = personList.get(0);
        personEntity.setAge(47);
        var updatedPerson = personStore.update(personEntity);
        var actualUpdatedPerson = personRepository.findById(personEntity.getId());
        assertTrue(actualUpdatedPerson.isPresent());
        assertEquals(updatedPerson, actualUpdatedPerson.get());
        assertEquals(47, actualUpdatedPerson.get().getAge());
    }

    @Test
    void deleteById() {
        var personEntity = personList.get(1);
        personStore.deleteById(personEntity.getId());
        var actualPersonEntity = personRepository.findById(personEntity.getId());
        assertNotNull(actualPersonEntity);
        assertTrue(actualPersonEntity.isEmpty());
    }

    @Test
    void findAll() {
        var personPage = personStore.findAll(0, 10);
        assertNotNull(personPage);
        assertEquals(2, personPage.getTotalElements());
        assertEquals(0, personPage.getNumber());
        assertEquals(1, personPage.getTotalPages());
    }

    @AfterEach
    void tearDown() {
        personRepository.deleteAll();
    }
}