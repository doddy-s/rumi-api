package com.doddysujatmiko.rumiapi.consumet.dtos;

import com.doddysujatmiko.rumiapi.consumet.ConsumetAnimeEntity;
import com.doddysujatmiko.rumiapi.consumet.enums.ProviderEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumetAnimeDto {
//    private Integer malId;

    private String consumetId;

    private String title;

    private String image;

    @Enumerated(EnumType.STRING)
    private ProviderEnum provider;

    public static ConsumetAnimeDto fromEntity(ConsumetAnimeEntity consumetAnimeEntity) {
        return ConsumetAnimeDto.builder()
//                .malId(consumetEntity.getMalId() == 0 ? null : consumetEntity.getMalId())
                .consumetId(consumetAnimeEntity.getConsumetId())
                .title(consumetAnimeEntity.getTitle())
                .image(consumetAnimeEntity.getImage())
                .provider(consumetAnimeEntity.getProvider())
                .build();
    }
}
