package com.doddysujatmiko.rumiapi.log;

import com.doddysujatmiko.rumiapi.common.BaseEntity;
import jakarta.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private LogLevel level;

    @Column(length = 2048)
    private String message;
}
