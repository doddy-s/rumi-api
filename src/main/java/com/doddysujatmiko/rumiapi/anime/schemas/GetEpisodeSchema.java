package com.doddysujatmiko.rumiapi.anime.schemas;

import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetAnimeDto;
import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetEpisodeDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetEpisodeSchema {
    ConsumetAnimeDto stream;
    List<ConsumetEpisodeDto> episodes;
}
