package com.doddysujatmiko.rumiapi.log;

import com.doddysujatmiko.rumiapi.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.boot.logging.LogLevel;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "logs")
public class LogEntity extends BaseEntity {
    private LogLevel level;

    private String message;
}
