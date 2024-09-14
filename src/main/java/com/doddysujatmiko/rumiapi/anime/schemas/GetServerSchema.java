package com.doddysujatmiko.rumiapi.anime.schemas;

import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetEpisodeDto;
import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetServerDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetServerSchema {
    ConsumetEpisodeDto episode;
    List<ConsumetServerDto> servers;
}
