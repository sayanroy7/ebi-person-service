package com.ebi.app.controller;

import com.ebi.app.EnableMockSecurityTestConfig;
import com.ebi.app.TestDataUtils;
import com.ebi.app.WithMockSecurity;
import com.ebi.app.model.web.request.PersonDTO;
import com.ebi.app.model.web.response.WebApiResponse;
import com.ebi.app.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = { PersonRestController.class, RestExceptionHandler.class,
        EnableMockSecurityTestConfig.class})
@EnableWebMvc
@AutoConfigureMockMvc
class PersonRestControllerTest {

    private static ObjectMapper objectMapper;

    private List<PersonDTO> personDTOS;

    private List<com.ebi.app.model.web.response.PersonDTO> personResponseDTOS;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @BeforeAll
    public static void init() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        personDTOS = TestDataUtils.getPersonsDTO(objectMapper);
        personResponseDTOS = TestDataUtils.getPersonsResponseDTO(objectMapper);
    }

    @Test
    @WithMockSecurity(scopes = "ebi:user")
    void getPerson() throws Exception {
        var personDTO = personResponseDTOS.get(0);
        var personID = UUID.randomUUID().toString();
        personDTO.setId(personID);
        when(personService.findById(anyString())).thenReturn(Optional.of(personDTO));
        var expectedResponse = WebApiResponse.success(personDTO);

        var requestBuilder = MockMvcRequestBuilders
                .get("/person/{id}", personID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        var result = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andReturn();
        var responseContent = result.getResponse().getContentAsString();
        assertNotNull(responseContent);
        JSONAssert.assertEquals(objectMapper.writeValueAsString(expectedResponse), responseContent, false);
    }

    @Test
    void getPersons() {
    }

    @Test
    void savePerson() {
    }

    @Test
    void updatePerson() {
    }

    @Test
    @WithMockSecurity(username = "admin", password = "admin", scopes = {"ebi:admin"})
    void deletePerson() throws Exception {
        var personDTO = personResponseDTOS.get(0);
        var personID = UUID.randomUUID().toString();
        personDTO.setId(personID);
        when(personService.deleteById(anyString())).thenReturn(Optional.of(personDTO));
        var expectedResponse = WebApiResponse.success(personDTO);

        var requestBuilder = MockMvcRequestBuilders
                .delete("/person/{id}", personID)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        var result = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andReturn();
        var responseContent = result.getResponse().getContentAsString();
        assertNotNull(responseContent);
        JSONAssert.assertEquals(objectMapper.writeValueAsString(expectedResponse), responseContent, false);
    }
}