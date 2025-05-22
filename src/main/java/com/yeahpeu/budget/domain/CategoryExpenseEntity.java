package com.yeahpeu.budget.domain;

import com.yeahpeu.category.domain.CategoryEntity;
import com.yeahpeu.common.domain.BaseEntity;
import com.yeahpeu.wedding.domain.WeddingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Builder
@Entity
@Table(
        name = "category_expense",
        indexes = {
                @Index(name = "idx_wedding_category", columnList = "wedding_id, category_id")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class CategoryExpenseEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "wedding_id", nullable = false)
    private WeddingEntity wedding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(name = "expense", nullable = false)
    private int expense;

    public void addExpense(int amount) {
        this.expense += amount;
    }

    public void subtractExpense(int amount) {
        this.expense -= amount;
    }

}
