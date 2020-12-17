package com.tabber.tabby.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.hibernate.annotations.Type;
import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "frontend_configurations")
@Builder(toBuilder = true)
public class FrontendConfigurationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty("id")
    private Integer id;

    @Column(name = "page_type")
    @JsonProperty("page_type")
    private String pageType;

    @JsonProperty("key")
    @Type(type = "jsonb")
    @Column(name = "key",columnDefinition = "jsonb")
    private String key;

    @Column(name = "value")
    @JsonProperty("value")
    private JsonNode value;

}
