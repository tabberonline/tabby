package com.tabber.tabby.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalProjectRequest {

    @JsonProperty("title")
    String title;

    @JsonProperty("id")
    Long id;

    @JsonProperty("link")
    String link;
}