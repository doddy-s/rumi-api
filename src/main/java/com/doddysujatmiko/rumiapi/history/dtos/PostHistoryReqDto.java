package com.doddysujatmiko.rumiapi.history.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class PostHistoryReqDto {
    @NotEmpty(message = "shall not empty")
    private String consumetEpisodeId;

    @NotNull(message = "shall not empty")
    @Range(min = 0)
    private Integer second;
}
