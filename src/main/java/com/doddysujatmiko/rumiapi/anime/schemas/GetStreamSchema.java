package com.doddysujatmiko.rumiapi.anime.schemas;

import com.doddysujatmiko.rumiapi.anime.dtos.AnimeDto;
import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetAnimeDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetStreamSchema {
    AnimeDto anime;
    List<ConsumetAnimeDto> streams;
}
