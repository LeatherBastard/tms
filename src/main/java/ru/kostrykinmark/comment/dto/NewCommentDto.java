package ru.kostrykinmark.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
@Builder
public class NewCommentDto {
   @NotBlank
   @Size(min =3,max = 500)
    private String text;
}
