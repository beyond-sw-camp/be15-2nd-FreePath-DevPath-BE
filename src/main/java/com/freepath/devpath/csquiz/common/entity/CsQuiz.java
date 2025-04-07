package com.freepath.devpath.csquiz.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="csquiz")
@Getter
@NoArgsConstructor
public class CsQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int csquizId;

    private String csquizContents;

    private int csquizAnswer;

    private String csquizExplanation;

    private String isCsquizSubmitted;
}
