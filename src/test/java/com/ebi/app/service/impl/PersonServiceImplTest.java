package com.ebi.app.service.impl;

import com.ebi.app.TestDataUtils;
import com.ebi.app.model.Person;
import com.ebi.app.store.PersonStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    private static ObjectMapper objectMapper;

    @Mock
    private PersonStore<Person> personStore;

    @InjectMocks
    private PersonServiceImpl personService;

    private List<Person> personList;

    @BeforeAll
    public static void init() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        personList = TestDataUtils.getPersons(objectMapper);
    }

    @Test
    void findById() {
        var person = personList.get(0);
        person.setId(UUID.randomUUID().toString());
        when(personStore.findById(anyString())).thenReturn(Optional.of(person));
        var personDTO = personService.findById(person.getId());
        assertNotNull(personDTO);
        assertTrue(personDTO.isPresent());
        var actualPersonDTO = personDTO.get();
        assertEquals(person.getId(), actualPersonDTO.getId());
        assertEquals(person.getFirstName(), actualPersonDTO.getFirstName());
        assertEquals(person.getLastName(), actualPersonDTO.getLastName());
        assertEquals(person.getAge(), actualPersonDTO.getAge());
        assertEquals(person.getFavouriteColour(),actualPersonDTO.getFavouriteColour());
    }

    @Test
    void findByIdNotFound() {
        when(personStore.findById(anyString())).thenReturn(Optional.empty());
        var personDTO = personService.findById(UUID.randomUUID().toString());
        assertNotNull(personDTO);
        assertTrue(personDTO.isEmpty());
    }

    @Test
    void save() throws IOException {
        var personDTOs = TestDataUtils.getPersonsDTO(objectMapper);
        var personDTO = personDTOs.get(0);
        when(personStore.save(any(Person.class))).thenReturn(personList.get(0));
        var savedPerson = personService.save(personDTO);
        assertNotNull(savedPerson);
        assertEquals(personDTO.getFirstName(), savedPerson.getFirstName());
        assertEquals(personDTO.getLastName(), savedPerson.getLastName());
        assertEquals(personDTO.getAge(), savedPerson.getAge());
        assertEquals(personDTO.getFavouriteColour(),savedPerson.getFavouriteColour());
    }

    @Test
    void saveNull() {
        var savedPerson = personService.save(null);
        assertNull(savedPerson);
    }

    @Test
    void update() throws IOException {
        var personDTOs = TestDataUtils.getPersonsDTO(objectMapper);
        var personDTO = personDTOs.get(0);
        var personID = UUID.randomUUID().toString();
        var existingPersonEntity = personList.get(0);
        existingPersonEntity.setId(personID);
        when(personStore.findById(personID)).thenReturn(Optional.of(existingPersonEntity));
        var updatedPersonEntity = Person.builder().id(personID).age(existingPersonEntity.getAge() + 8)
                .firstName(existingPersonEntity.getFirstName()).lastName(existingPersonEntity.getLastName())
                .favouriteColour(existingPersonEntity.getFavouriteColour()).build();
        when(personStore.update(any(Person.class))).thenReturn(updatedPersonEntity);

        var personUpdated = personService.update(personDTO, personID);
        assertNotNull(personUpdated);
        assertTrue(personUpdated.isPresent());
        assertEquals(8, personUpdated.get().getAge() - existingPersonEntity.getAge());
    }

    @Test
    void updateNotFound() throws IOException {
        var personDTOs = TestDataUtils.getPersonsDTO(objectMapper);
        var personDTO = personDTOs.get(0);
        var personID = UUID.randomUUID().toString();
        var existingPersonEntity = personList.get(0);
        existingPersonEntity.setId(personID);
        when(personStore.findById(personID)).thenReturn(Optional.empty());

        var personUpdated = personService.update(personDTO, personID);
        assertNotNull(personUpdated);
        assertTrue(personUpdated.isEmpty());
    }

    @Test
    void deleteById() {
        var personID = UUID.randomUUID().toString();
        var existingPersonEntity = personList.get(0);
        existingPersonEntity.setId(personID);
        when(personStore.findById(personID)).thenReturn(Optional.of(existingPersonEntity));
        doNothing().when(personStore).deleteById(personID);

        var personDeleted = personService.deleteById(personID);
        assertNotNull(personDeleted);
        assertTrue(personDeleted.isPresent());
        var actualPersonDTO = personDeleted.get();
        assertEquals(existingPersonEntity.getId(), actualPersonDTO.getId());
        assertEquals(existingPersonEntity.getFirstName(), actualPersonDTO.getFirstName());
        assertEquals(existingPersonEntity.getLastName(), actualPersonDTO.getLastName());
        assertEquals(existingPersonEntity.getAge(), actualPersonDTO.getAge());
        assertEquals(existingPersonEntity.getFavouriteColour(),actualPersonDTO.getFavouriteColour());
    }

    @Test
    void deleteByIdNotFound() {
        var personID = UUID.randomUUID().toString();
        var existingPersonEntity = personList.get(0);
        existingPersonEntity.setId(personID);
        when(personStore.findById(personID)).thenReturn(Optional.empty());

        var personDeleted = personService.deleteById(personID);
        assertNotNull(personDeleted);
        assertTrue(personDeleted.isEmpty());
    }

    @Test
    void findAll() {
        var page = new PageImpl<>(personList);
        when(personStore.findAll(anyInt(), anyInt())).thenReturn(page);
        var persons = personService.findAll(0, 5);
        assertNotNull(persons);
        assertEquals(2, persons.getPerson().size());
        assertEquals(2L, persons.getTotalItems());
        assertEquals(0, persons.getCurrentPage());
    }
}