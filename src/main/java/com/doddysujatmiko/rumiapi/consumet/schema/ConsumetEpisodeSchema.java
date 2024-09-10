package com.doddysujatmiko.rumiapi.consumet.schema;

import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetEpisodeDto;
import com.doddysujatmiko.rumiapi.consumet.enums.ProviderEnum;
import lombok.Data;

import java.util.List;

@Data
public class ConsumetEpisodeSchema {
    ProviderEnum provider;
    List<ConsumetEpisodeDto> list;
}
