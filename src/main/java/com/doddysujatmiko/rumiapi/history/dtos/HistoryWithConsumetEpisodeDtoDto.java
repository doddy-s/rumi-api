package com.doddysujatmiko.rumiapi.history.dtos;

import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetAnimeDto;
import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetEpisodeDto;
import com.doddysujatmiko.rumiapi.history.HistoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryWithConsumetEpisodeDtoDto {
    private ConsumetAnimeDto consumetAnime;

    private ConsumetEpisodeDto consumetEpisode;

    private Integer second;

    public static HistoryWithConsumetEpisodeDtoDto fromEntity(HistoryEntity historyEntity) {
        return HistoryWithConsumetEpisodeDtoDto.builder()
                .consumetAnime(ConsumetAnimeDto.fromEntity(historyEntity.getConsumetAnime()))
                .consumetEpisode(ConsumetEpisodeDto.fromEntity(Objects.requireNonNull(historyEntity.getConsumetAnime().getConsumetEpisodes().stream().filter(consumetEpisodeEntity -> Objects.equals(consumetEpisodeEntity.getNumber(), historyEntity.getEpisodeNumber())).findFirst().orElse(null))))
                .second(historyEntity.getSecond())
                .build();
    }
}