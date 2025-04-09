package com.freepath.devpath.csquiz.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="csquiz")
@Getter
@NoArgsConstructor
public class CsQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int csquizId;
    @Setter
    private String csquizContents;
    @Setter
    private int csquizAnswer;
    @Setter
    private String csquizExplanation;
    @Setter
    private String isCsquizSubmitted;
}
