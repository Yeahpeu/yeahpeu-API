package com.yeahpeu.budget.domain;

import com.yeahpeu.common.domain.BaseEntity;
import com.yeahpeu.wedding.domain.WeddingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Entity
@Getter
@Table(name = "budget")
public class BudgetEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
    private WeddingEntity wedding;

    @Column(nullable = false)
    private Long totalBudget;

    @Column(nullable = false)
    private Long usedBudget;

    public void addUsedBudget(int amount) {
        this.usedBudget += amount;
    }

    public void subtractUsedBudget(int amount) {
        this.usedBudget -= amount;
    }

}
