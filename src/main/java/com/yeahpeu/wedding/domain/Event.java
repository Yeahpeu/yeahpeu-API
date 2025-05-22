package com.yeahpeu.wedding.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
    private String title;
    private int mainCategoryId;
    private int subCategoryId;
    private int price;
    private String location;
    private int weeksBefore;
}
