package com.tabber.tabby.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "experience_widgets")
@Builder(toBuilder = true)
public class ExperienceWidgetEntity {


    @Id
    @Column(name = "experience_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("experience_id")
    private Integer experienceId;

    @JsonProperty("type")
    @Column(name = "type")
    private String type;

    @JsonProperty("company_name")
    @Column(name = "company_name")
    private String companyName;

    @JsonProperty("description")
    @Column(name = "description")
    private String description;

    @JsonProperty("start_date")
    @Column(name = "start_date")
    private String startDate;

    @JsonProperty("end_date")
    @Column(name = "end_date")
    private String endDate;

    @JsonProperty("cloud_certification_link")
    @Column(name = "cloud_certification_link")
    private String cloudCertificationLink;

    @JsonProperty("experience_user_id")
    @Column(name = "experience_user_id")
    private Long experienceUserId;

}
