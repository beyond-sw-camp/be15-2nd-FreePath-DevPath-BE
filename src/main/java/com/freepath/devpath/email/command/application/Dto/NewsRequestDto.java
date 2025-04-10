package com.freepath.devpath.email.command.application.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class NewsRequestDto {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotBlank(message = "Link cannot be blank")
    private String link;
    @NotBlank(message = "Content cannot be blank")
    private String content;
    @NotNull(message = "Mailing date cannot be null")
    private Date mailingDate;
}