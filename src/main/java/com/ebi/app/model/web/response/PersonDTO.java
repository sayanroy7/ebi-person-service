package com.ebi.app.model.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonDTO {

    private String id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private Integer age;

    @JsonProperty("favourite_colour")
    private String favouriteColour;

}
