package com.yeahpeu.event.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.yeahpeu.category.domain.CategoryEntity;
import com.yeahpeu.common.domain.BaseEntity;
import com.yeahpeu.event.service.command.EventCreateCommand;
import com.yeahpeu.task.domain.TaskEntity;
import com.yeahpeu.wedding.domain.WeddingEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "event")
public class EventEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "wedding_id", nullable = false)
    private WeddingEntity wedding;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @Column(name = "location")
    private String location;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "main_category_id")
    private CategoryEntity mainCategory;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sub_category_id")
    private CategoryEntity subCategory;

    @Column(name = "price")
    private int price;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskEntity> taskList = new ArrayList<>();

    @Builder
    private EventEntity(WeddingEntity wedding, String title, ZonedDateTime date, String location,
                        CategoryEntity mainCategory, CategoryEntity subCategory, int price, boolean completed) {
        this.wedding = wedding;
        this.title = title;
        this.date = date;
        this.location = location;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.price = price;
        this.completed = completed;
    }

    public static EventEntity of(WeddingEntity weddingEntity, EventCreateCommand eventCreateCommand,
                                 CategoryEntity mainCategoryEntity, CategoryEntity subcategoryEntity) {
        return EventEntity.builder()
                .wedding(weddingEntity)
                .title(eventCreateCommand.getTitle())
                .date(eventCreateCommand.getDate())
                .location(eventCreateCommand.getLocation())
                .mainCategory(mainCategoryEntity)
                .subCategory(subcategoryEntity)
                .price(eventCreateCommand.getPrice())
                .build();
    }

    public void updateDetails(EventCreateCommand eventCreateCommand, CategoryEntity mainCategoryEntity,
                              CategoryEntity subcategoryEntity) {
        title = eventCreateCommand.getTitle();
        date = eventCreateCommand.getDate();
        location = eventCreateCommand.getLocation();
        mainCategory = mainCategoryEntity;
        subCategory = subcategoryEntity;
        price = eventCreateCommand.getPrice();
    }

    public void updateStatus(Boolean isCompleted) {
        this.completed = isCompleted;
    }
}
