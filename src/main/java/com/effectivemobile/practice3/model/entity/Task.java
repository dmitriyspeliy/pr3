package com.effectivemobile.practice3.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Task {
    Long id;
    String title;
    String description;
}
