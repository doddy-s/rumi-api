package com.doddysujatmiko.rumiapi.jikan;

import com.doddysujatmiko.rumiapi.anime.AnimeEntity;
import com.doddysujatmiko.rumiapi.anime.enums.SeasonEnum;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JikanMapper {
    public AnimeEntity toAnimeEntity(Map<String, Object> anime) {
        var animeEntity = new AnimeEntity();
        animeEntity.setMalId(anime.get("mal_id") == null ? 0 : (int) anime.get("mal_id"));
        animeEntity.setEnglishTitle((String) anime.get("title_english"));
        animeEntity.setJapaneseTitle((String) anime.get("title_japanese"));
        animeEntity.setRating(anime.get("score") == null ? 0d : (double) anime.get("score"));
        animeEntity.setDescription((String) anime.get("synopsis"));
        animeEntity.setPicture((String) ((Map<String, Object>)((Map<String, Object>) anime.get("images")).get("jpg")).get("image_url"));
        animeEntity.setReleaseYear(anime.get("year") == null ? 0 : (int) anime.get("year"));
        animeEntity.setReleaseSeason(anime.get("season") ==  null ? null : SeasonEnum.valueOf(((String) anime.get("season")).toUpperCase()));

        return animeEntity;
    }
}
