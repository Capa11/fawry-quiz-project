package com.example.Quiz.payload;

import lombok.Builder;
import lombok.*;

@Getter @Setter
public class AssignmentsListElement {
    private Long id;
    private String title;
    private String description;

    public AssignmentsListElement(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
