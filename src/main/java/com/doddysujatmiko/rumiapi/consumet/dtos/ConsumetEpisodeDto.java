package com.doddysujatmiko.rumiapi.consumet.dtos;

import com.doddysujatmiko.rumiapi.consumet.ConsumetEpisodeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumetEpisodeDto {
    private String consumetId;

    private Integer number;

    private String title;

    public static ConsumetEpisodeDto fromEntity(ConsumetEpisodeEntity consumetEpisodeEntity) {
        return ConsumetEpisodeDto.builder()
                .consumetId(consumetEpisodeEntity.getConsumetId())
                .number(consumetEpisodeEntity.getNumber())
                .title(consumetEpisodeEntity.getTitle())
                .build();
    }
}
