package com.doddysujatmiko.rumiapi.consumet.dtos;

import com.doddysujatmiko.rumiapi.consumet.ConsumetEntity;
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
public class ConsumetDto {
//    private Integer malId;

    private String consumetId;

    private String title;

    private String image;

    @Enumerated(EnumType.STRING)
    private ProviderEnum provider;

    public static ConsumetDto fromEntity(ConsumetEntity consumetEntity) {
        return ConsumetDto.builder()
//                .malId(consumetEntity.getMalId() == 0 ? null : consumetEntity.getMalId())
                .consumetId(consumetEntity.getConsumetId())
                .title(consumetEntity.getTitle())
                .image(consumetEntity.getImage())
                .provider(consumetEntity.getProvider())
                .build();
    }
}
