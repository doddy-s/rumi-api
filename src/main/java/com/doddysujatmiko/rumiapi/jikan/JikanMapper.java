package com.doddysujatmiko.rumiapi.jikan;

import com.doddysujatmiko.rumiapi.anime.AnimeEntity;
import com.doddysujatmiko.rumiapi.anime.enums.SeasonEnum;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JikanMapper {
    public AnimeEntity toAnimeEntity(JSONObject anime) {
        return AnimeEntity.builder()
                .malId(anime.optInt("mal_id", 0))
                .englishTitle(anime.optString("title_english", null))
                .japaneseTitle(anime.optString("title_japanese", null))
                .rating(anime.has("score") ? anime.optFloat("score") : 0f)
                .description(anime.optString("synopsis", null))
                .picture(anime.optJSONObject("images")
                        .optJSONObject("jpg")
                        .optString("image_url", null))
                .releaseYear(anime.optInt("year", 0))
                .releaseSeason(anime.isNull("season") ?
                        null : SeasonEnum.valueOf(anime.optString("season").toUpperCase()))
                .build();
    }
}
