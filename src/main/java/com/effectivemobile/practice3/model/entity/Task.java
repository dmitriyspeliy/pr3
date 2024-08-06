package com.effectivemobile.practice3.model.entity;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Task {
    private Long id;
    private String title;
    private String description;
}
