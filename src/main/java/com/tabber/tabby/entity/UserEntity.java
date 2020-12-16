package com.tabber.tabby.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.util.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Builder(toBuilder = true)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "sub")
    private String sub;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "locale")
    private String locale;

    @Column(name = "resume_present")
    private Boolean resumePresent;

    @OneToMany
    @JoinColumn(name = "rank_widget_user_id")
    private List<RankWidgetEntity> rankWidgets;

    @OneToMany
    @JoinColumn(name = "contest_widget_user_id")
    private List<ContestWidgetEntity> contestWidgets;

    @JsonManagedReference
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private PortfolioEntity portfolio;

    @Column(name="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
