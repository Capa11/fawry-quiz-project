package com.example.Quiz.payload;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NotNull

public class AdminRoomDTO {
    private String name;
    private Boolean joinByLink = true;
    private Boolean isCommentAllowed = false;
    private Boolean isQuestionAllowed = false;
    private Boolean isVerified = false;
}
