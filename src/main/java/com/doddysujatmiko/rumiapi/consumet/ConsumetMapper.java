package com.doddysujatmiko.rumiapi.consumet;

import com.doddysujatmiko.rumiapi.consumet.enums.ProviderEnum;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class ConsumetMapper {
    public ConsumetAnimeEntity toConsumetEntity(JSONObject consumet, ProviderEnum by) {
        return ConsumetAnimeEntity.builder()
                .malId(consumet.optInt("malId", 0))
                .consumetId(consumet.optString("id", null))
                .title(consumet.optString("title", null))
                .image(consumet.optString("image", null))
                .provider(by)
                .build();
    }

    public ConsumetEpisodeEntity toConsumetEpisodeEntity(JSONObject episode, ConsumetAnimeEntity consumetAnimeEntity) {
        return ConsumetEpisodeEntity.builder()
                .consumetId(episode.optString("id", null))
                .title(episode.optString("title", episode.optString("id", null)))
                .consumetAnime(consumetAnimeEntity)
                .build();
    }
}
