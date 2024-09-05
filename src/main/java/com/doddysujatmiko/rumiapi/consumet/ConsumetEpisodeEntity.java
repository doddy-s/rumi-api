package com.doddysujatmiko.rumiapi.consumet;

import com.doddysujatmiko.rumiapi.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consumet_episodes")
public class ConsumetEpisodeEntity extends BaseEntity {
    private String consumetId;

    private String title;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private ConsumetAnimeEntity consumetAnime;

    @OneToMany(mappedBy = "consumetEpisode", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ConsumetServerEntity> consumetServers;
}
