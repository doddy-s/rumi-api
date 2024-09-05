package com.doddysujatmiko.rumiapi.consumet.dtos;

import com.doddysujatmiko.rumiapi.consumet.ConsumetServerEntity;
import com.doddysujatmiko.rumiapi.consumet.enums.ServerEnum;
import com.doddysujatmiko.rumiapi.consumet.enums.VideoQualityEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumetServerDto {
    private String url;

    private VideoQualityEnum videoQuality;

    private ServerEnum server;

    public static ConsumetServerDto fromEntity(ConsumetServerEntity server) {
        return ConsumetServerDto.builder()
                .url(server.getUrl())
                .videoQuality(server.getVideoQuality())
                .server(server.getServer())
                .build();
    }
}
