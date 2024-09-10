package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.anime.enums.SeasonEnum;
import com.doddysujatmiko.rumiapi.common.BaseEntity;
import com.doddysujatmiko.rumiapi.consumet.ConsumetAnimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    @ManyToMany(targetEntity = ConsumetAnimeEntity.class,
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.LAZY)
    @JoinTable(
            name = "anime_consumet",
            joinColumns = {
                    @JoinColumn(name = "anime_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "consumet_id")
            }
    )
    private List<ConsumetAnimeEntity> consumets = new ArrayList<>();

    private Boolean hasConsumetsCache = false;

    public Boolean isAiring() {
        var month = LocalDate.now().getMonthValue();
        SeasonEnum currentSeason = switch (month) {
            case 1, 2, 3 -> SeasonEnum.WINTER;
            case 4, 5, 6 -> SeasonEnum.SPRING;
            case 7, 8, 9 -> SeasonEnum.SUMMER;
            case 10, 11, 12 -> SeasonEnum.FALL;
            default -> throw new IllegalStateException("Unexpected value: " + month);
        };

        var year = LocalDate.now().getYear();

        return season == currentSeason && this.year == year;
    }
}
