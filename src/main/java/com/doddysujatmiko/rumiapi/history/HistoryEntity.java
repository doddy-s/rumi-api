package com.doddysujatmiko.rumiapi.history;

import com.doddysujatmiko.rumiapi.auth.UserEntity;
import com.doddysujatmiko.rumiapi.common.BaseEntity;
import com.doddysujatmiko.rumiapi.consumet.ConsumetAnimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "histories")
public class HistoryEntity extends BaseEntity {
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private UserEntity user;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private ConsumetAnimeEntity consumetAnime;

    private Integer episodeNumber;

    private Integer second;
}
