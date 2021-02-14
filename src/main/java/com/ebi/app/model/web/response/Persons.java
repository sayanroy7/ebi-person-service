package com.ebi.app.model.web.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Persons {

    @ApiModelProperty(value = "current page number of the paginated response", example = "0")
    private Integer currentPage;

    @ApiModelProperty(value = "Toatal number of persons available in the page query", example = "100")
    private Long totalItems;

    @ApiModelProperty(value = "Toatal number of pages available in the page query", example = "10")
    private Integer totalPages;

    @ApiModelProperty(value = "List of person included in the current page")
    private List<PersonDTO> person;

}
