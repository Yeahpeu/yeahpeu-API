package com.yeahpeu.entities;

import com.yeahpeu.entities.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "schedule")
public class ScheduleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_id", nullable = false)
    private WeddingEntity wedding;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @Column(name = "location")
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_id")
    private CategoryEntity mainCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private CategoryEntity subCategory;

    @Column(name = "price")
    private String price;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted = false;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistEntity> checklists = new ArrayList<>();
}
