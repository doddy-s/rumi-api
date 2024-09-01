package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.common.BaseEntity;
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
@Table(name = "genres")
public class GenreEntity extends BaseEntity  {
    @Column(nullable = false)
    private Integer malId;

    private String name;

    @JsonIgnore
    @ManyToMany(targetEntity = AnimeEntity.class, mappedBy = "genres", fetch = FetchType.LAZY)
    private List<AnimeEntity> animes;
}
