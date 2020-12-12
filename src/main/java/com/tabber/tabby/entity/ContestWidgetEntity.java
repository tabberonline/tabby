package com.tabber.tabby.entity;

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
@Table(name = "contest_widgets")
@Builder(toBuilder = true)
public class ContestWidgetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long contestWidgetId;

    @Column(name = "website_id")
    private Integer websiteId;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "contest_name")
    private String contestName;

    @Column(name = "website_username")
    private String websiteUsername;

    @Column(name ="contest_widget_user_id")
    private Long userId;

    @Column(name="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
