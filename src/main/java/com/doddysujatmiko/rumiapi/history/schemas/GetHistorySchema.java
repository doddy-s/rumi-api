package com.doddysujatmiko.rumiapi.history.schemas;

import com.doddysujatmiko.rumiapi.auth.dtos.UserDto;
import com.doddysujatmiko.rumiapi.common.SimplePage;
import com.doddysujatmiko.rumiapi.history.dtos.HistoryWithConsumetEpisodeDtoDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetHistorySchema {
    private UserDto user;
    private SimplePage<HistoryWithConsumetEpisodeDtoDto> historyPage;
}
