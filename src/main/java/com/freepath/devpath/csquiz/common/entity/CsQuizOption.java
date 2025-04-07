package com.freepath.devpath.csquiz.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="csquiz_option")
@Getter
@NoArgsConstructor
public class CsQuizOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int optionID;
    @Setter
    private int csquizId;
    @Setter
    private int optionNo;
    @Setter
    private String optionContents;

}
