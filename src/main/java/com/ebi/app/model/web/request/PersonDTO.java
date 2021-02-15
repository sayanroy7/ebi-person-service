package com.ebi.app.model.web.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonDTO {

    @JsonProperty("first_name")
    @NotEmpty(message = "first_name cannot be empty")
    private String firstName;

    @JsonProperty("last_name")
    @NotEmpty(message = "last_name cannot be empty")
    private String lastName;

    @Min(1)
    private Integer age;

    @JsonProperty("favourite_colour")
    private String favouriteColour;

}
