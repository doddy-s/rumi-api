package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.anime.enums.SeasonEnum;
import com.doddysujatmiko.rumiapi.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animes")
public class AnimeEntity extends BaseEntity {
    @Column(nullable = false)
    private int malId;

//    private String gogoanimeId;

    private String englishTitle;

    private String japaneseTitle;

    private double rating;

    @Column(length = 2048)
    private String description;

    private String picture;

    private int releaseYear;

    private SeasonEnum releaseSeason;
}
