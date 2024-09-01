package com.doddysujatmiko.rumiapi.anime.dtos;

import com.doddysujatmiko.rumiapi.anime.AnimeEntity;
import com.doddysujatmiko.rumiapi.anime.enums.SeasonEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimeDto {
    private Integer malId;
    private String title;
    private String englishTitle;
    private String japaneseTitle;
    private Float score;
    private String synopsis;
    private String image;
    private Integer year;

    @Enumerated(EnumType.STRING)
    private SeasonEnum season;

    private List<GenreDto> genres = new ArrayList<>();
    private List<StudioDto> studios = new ArrayList<>();

    public static AnimeDto fromEntity(AnimeEntity animeEntity) {
        return AnimeDto.builder()
                .malId(animeEntity.getMalId())
                .title(animeEntity.getTitle())
                .englishTitle(animeEntity.getEnglishTitle())
                .japaneseTitle(animeEntity.getJapaneseTitle())
                .score(animeEntity.getScore())
                .synopsis(animeEntity.getSynopsis())
                .image(animeEntity.getImage())
                .year(animeEntity.getYear())
                .season(animeEntity.getSeason())
                .genres(animeEntity.getGenres().stream().map(GenreDto::fromEntity).toList())
                .studios(animeEntity.getStudios().stream().map(StudioDto::fromEntity).toList())
                .build();
    }
}
