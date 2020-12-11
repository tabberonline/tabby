package com.tabber.tabby.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ContestWidgetRequest {

    @JsonProperty("website_id")
    Integer websiteId;

    @JsonProperty("rank")
    Integer rank;

    @JsonProperty("username")
    String username;

    @JsonProperty("contest_name")
    String contestName;
}