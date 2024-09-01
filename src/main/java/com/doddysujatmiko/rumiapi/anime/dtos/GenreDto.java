package com.doddysujatmiko.rumiapi.anime.dtos;

import com.doddysujatmiko.rumiapi.anime.GenreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {
    private Integer malId;

    private String name;

    public static GenreDto fromEntity(GenreEntity genreEntity) {
        return GenreDto.builder()
                .malId(genreEntity.getMalId())
                .name(genreEntity.getName())
                .build();
    }
}
