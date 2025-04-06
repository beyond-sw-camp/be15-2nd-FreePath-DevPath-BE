package com.freepath.devpath.common.auth.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken implements Serializable {
    private String token;
}
