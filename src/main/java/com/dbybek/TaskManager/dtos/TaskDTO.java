package com.dbybek.TaskManager.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "user",
        "completed"
})
public class TaskDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    // ✅ Response-only field
    private String username;

    private boolean completed;
}
