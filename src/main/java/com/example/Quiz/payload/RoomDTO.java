package com.example.Quiz.payload;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NotNull
public class RoomDTO {
    private String name;
    private Boolean joinByLink = true;
    private Boolean isCommentAllowed = true;
    private Boolean isQuestionAllowed = true;



}
