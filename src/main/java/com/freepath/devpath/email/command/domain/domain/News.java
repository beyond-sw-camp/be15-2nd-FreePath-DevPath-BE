package com.freepath.devpath.email.command.domain.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "IT_NEWS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itNewsId;

    @Column(name = "it_news_title")
    private String title;
    @Column(name = "it_news_link")
    private String link;
    @Column(name = "it_news_contents")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "mailing_date")
    private Date mailingDate;

    public void setMailingDate(Date date) {
        this.mailingDate = date;
    }

}
