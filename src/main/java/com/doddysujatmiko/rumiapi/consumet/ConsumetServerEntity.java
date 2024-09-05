package com.doddysujatmiko.rumiapi.consumet;

import com.doddysujatmiko.rumiapi.common.BaseEntity;
import com.doddysujatmiko.rumiapi.consumet.enums.ServerEnum;
import com.doddysujatmiko.rumiapi.consumet.enums.VideoQualityEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consumet_servers")
public class ConsumetServerEntity extends BaseEntity {
    @Column(unique = true, length = 2048)
    private String url;

    @Enumerated(EnumType.STRING)
    private VideoQualityEnum videoQuality;

    @Enumerated(EnumType.STRING)
    private ServerEnum server;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private ConsumetEpisodeEntity consumetEpisode;
}
