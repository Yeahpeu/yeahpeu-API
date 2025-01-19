package com.yeahpeu.entities;

import com.yeahpeu.entities.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "checklist")
public class ChecklistEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isCompleted;

}
