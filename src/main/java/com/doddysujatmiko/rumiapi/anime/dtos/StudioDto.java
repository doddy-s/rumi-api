package com.doddysujatmiko.rumiapi.anime.dtos;

import com.doddysujatmiko.rumiapi.anime.StudioEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudioDto {
    private Integer malId;

    private String name;

    public static StudioDto fromEntity(StudioEntity studioEntity) {
        return StudioDto.builder()
                .malId(studioEntity.getMalId())
                .name(studioEntity.getName())
                .build();
    }
}
