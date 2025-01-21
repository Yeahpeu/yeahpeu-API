package com.yeahpeu.entities;

import com.yeahpeu.common.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "budget")
public class BudgetEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_id", nullable = false)
    private WeddingEntity wedding;

    @Column(nullable = false)
    private Long totalBudget;

    @Column(nullable = false)
    private Long usedBudget;

}
