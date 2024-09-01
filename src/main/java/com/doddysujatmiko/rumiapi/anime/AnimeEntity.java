package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.anime.enums.SeasonEnum;
import com.doddysujatmiko.rumiapi.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animes")
public class AnimeEntity extends BaseEntity {
    @Column(nullable = false)
    private Integer malId;

    private String title;

    private String englishTitle;

    private String japaneseTitle;

    private Float score;

    @Column(length = 2048)
    private String synopsis;

    private String image;

    private Integer year;

    @Enumerated(EnumType.STRING)
    private SeasonEnum season;

    @ManyToMany(targetEntity = GenreEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "anime_genre",
            joinColumns = {
                    @JoinColumn(name = "anime_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "genre_id")
            }
    )
    private List<GenreEntity> genres = new ArrayList<>();

    @ManyToMany(targetEntity = StudioEntity.class,
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "anime_studio",
            joinColumns = {
                    @JoinColumn(name = "anime_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "studio_id")
            }
    )
    private List<StudioEntity> studios = new ArrayList<>();
}
