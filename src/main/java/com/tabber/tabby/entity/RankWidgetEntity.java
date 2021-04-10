package com.tabber.tabby.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rank_widgets")
@Builder(toBuilder = true)
public class RankWidgetEntity {
    @Id
    @Column(name = "id")
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankWidgetId;

    @Column(name = "website_id")
    @JsonProperty("website_id")
    private Integer websiteId;

    @Column(name = "rank")
    @JsonProperty("rank")
    private Integer rank;

    @Column(name = "website_username")
    @JsonProperty("website_username")
    private String websiteUsername;

    @Column(name = "link")
    @JsonProperty("link")
    private String link;

    @Column(name ="rank_widget_user_id")
    @JsonProperty("rank_widget_user_id")
    private Long userId;

    @Column(name = "invisible")
    @JsonProperty("invisible")
    private Boolean invisible = false;

    @Column(name="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @JsonProperty("created_at")
    private Date createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty("updated_at")
    private Date updatedAt;
}
