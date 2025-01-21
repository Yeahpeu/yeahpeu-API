package com.yeahpeu.entities;

import com.yeahpeu.common.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "category_expense")
public class CategoryExpenseEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_id", nullable = false)
    private WeddingEntity wedding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(name = "expense", nullable = false)
    private int expense;
}
