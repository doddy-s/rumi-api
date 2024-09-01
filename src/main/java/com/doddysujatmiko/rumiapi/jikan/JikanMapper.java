package com.doddysujatmiko.rumiapi.jikan;

import com.doddysujatmiko.rumiapi.anime.*;
import com.doddysujatmiko.rumiapi.anime.enums.SeasonEnum;
import com.doddysujatmiko.rumiapi.log.LogService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JikanMapper {
    private final GenreRepository genreRepository;
    private final StudioRepository studioRepository;
    private final LogService logService;

    @Autowired
    public JikanMapper(GenreRepository genreRepository, StudioRepository studioRepository, LogService logService) {
        this.genreRepository = genreRepository;
        this.studioRepository = studioRepository;
        this.logService = logService;
    }

    public AnimeEntity toAnimeEntity(JSONObject anime) {
        List<GenreEntity> genreEntities = new ArrayList<>();
        for(var genre : anime.getJSONArray("genres"))
            genreEntities.add(genreRepository.findByMalId(((JSONObject) genre).optInt("mal_id", 0)));
        for(var genre : anime.getJSONArray("themes"))
            genreEntities.add(genreRepository.findByMalId(((JSONObject) genre).optInt("mal_id", 0)));


        List<StudioEntity> studioEntities = new ArrayList<>();
        for(var studio : anime.getJSONArray("studios")) {
            StudioEntity studioEntity = studioRepository.findByMalId(((JSONObject) studio).optInt("mal_id", 0));
            if(studioEntity == null) {
                studioEntity = studioRepository.save(StudioEntity.builder()
                        .malId(((JSONObject) studio).optInt("mal_id", 0))
                        .name(((JSONObject) studio).optString("name", "noname"))
                        .build());
            }
            studioEntities.add(studioEntity);
        }

        logService.logInfo("Mapping anime "+anime.optString("title", null)+" by "+(studioEntities.stream().findFirst().orElse(StudioEntity.builder().name("nostudio").build())).getName());

        return AnimeEntity.builder()
                .malId(anime.optInt("mal_id", 0))
                .title(anime.optString("title", null))
                .englishTitle(anime.optString("title_english", null))
                .japaneseTitle(anime.optString("title_japanese", null))
                .score(anime.has("score") ? anime.optFloat("score") : 0f)
                .synopsis(anime.optString("synopsis", null))
                .image(anime.optJSONObject("images")
                        .optJSONObject("jpg")
                        .optString("image_url", null))
                .year(anime.optInt("year", 0))
                .season(anime.isNull("season") ?
                        null : SeasonEnum.valueOf(anime.optString("season").toUpperCase()))
                .genres(genreEntities)
                .studios(studioEntities)
                .build();
    }

    public GenreEntity toGenreEntity(JSONObject genre) {
        return GenreEntity.builder()
                .malId(genre.optInt("mal_id", 0))
                .name(genre.optString("name"))
                .build();
    }
}
