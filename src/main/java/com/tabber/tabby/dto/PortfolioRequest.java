package com.tabber.tabby.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioRequest {

    @JsonProperty("picture_url")
    String pictureUrl;

    @JsonProperty("title")
    String title;

    @JsonProperty("description")
    String description;
}