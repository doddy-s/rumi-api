package com.doddysujatmiko.rumiapi.consumet;

import com.doddysujatmiko.rumiapi.consumet.enums.ProviderEnum;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class ConsumetMapper {
    public ConsumetEntity toConsumetEntity(JSONObject consumet, ProviderEnum by) {
        return ConsumetEntity.builder()
                .malId(consumet.optInt("malId", 0))
                .consumetId(consumet.optString("id", null))
                .title(consumet.optString("title", null))
                .image(consumet.optString("image", null))
                .provider(by)
                .build();
    }
}
