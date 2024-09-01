package com.doddysujatmiko.rumiapi.common;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "commons")
public class CommonEntity extends BaseEntity {
    private String key;
    private Integer value;
}
