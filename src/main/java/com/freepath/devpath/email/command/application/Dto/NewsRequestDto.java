package com.freepath.devpath.email.command.application.Dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class NewsRequestDto {
    private String title;
    private String link;
    private String content;
    private Date mailingDate;
}