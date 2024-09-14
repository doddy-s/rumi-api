package com.doddysujatmiko.rumiapi.history.dtos;

import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetAnimeDto;
import com.doddysujatmiko.rumiapi.history.HistoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDto {
    private ConsumetAnimeDto consumetAnime;

    private Integer episodeNumber;

    private Integer second;

    public static HistoryDto fromEntity(HistoryEntity historyEntity) {
        return HistoryDto.builder()
                .consumetAnime(ConsumetAnimeDto.fromEntity(historyEntity.getConsumetAnime()))
                .episodeNumber(historyEntity.getEpisodeNumber())
                .second(historyEntity.getSecond())
                .build();
    }
}
