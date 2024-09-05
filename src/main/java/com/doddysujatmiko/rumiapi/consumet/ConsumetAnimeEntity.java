package com.doddysujatmiko.rumiapi.consumet;

import com.doddysujatmiko.rumiapi.anime.AnimeEntity;
import com.doddysujatmiko.rumiapi.common.BaseEntity;
import com.doddysujatmiko.rumiapi.consumet.enums.ProviderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consumets")
public class ConsumetAnimeEntity extends BaseEntity {
    private Integer malId;

    private String consumetId;

    private String title;

    private String image;

    @Enumerated(EnumType.STRING)
    private ProviderEnum provider;

    @JsonIgnore
    @ManyToMany(targetEntity = AnimeEntity.class, mappedBy = "consumets", fetch = FetchType.LAZY)
    private List<AnimeEntity> animes;

    @OneToMany(mappedBy = "consumetAnime", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ConsumetEpisodeEntity> consumetEpisodes;
}
