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
@Table(name = "course_widgets")
@Builder(toBuilder = true)
public class CourseWidgetEntity {

    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("course_id")
    private Integer courseId;

    @JsonProperty("issuer")
    @Column(name = "issuer")
    private String issuer;

    @JsonProperty("course_name")
    @Column(name = "course_name")
    private String courseName;

    @JsonProperty("certificate_link")
    @Column(name = "certificate_link")
    private String certificateLink;

    @JsonProperty("course_user_id")
    @Column(name = "course_user_id")
    private Long courseUserId;
}
