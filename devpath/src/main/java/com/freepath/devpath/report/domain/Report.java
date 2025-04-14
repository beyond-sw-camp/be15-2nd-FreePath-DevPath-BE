package com.freepath.devpath.report.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "REPORT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportId;

    private int reporterId;
    private Integer postId;
    private Integer commentId;
    private Date reportedAt;
}
