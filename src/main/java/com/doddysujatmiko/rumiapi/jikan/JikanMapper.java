package com.doddysujatmiko.rumiapi.jikan;

import com.doddysujatmiko.rumiapi.anime.*;
import com.doddysujatmiko.rumiapi.anime.dtos.AnimeDto;
import com.doddysujatmiko.rumiapi.anime.enums.SeasonEnum;
import com.doddysujatmiko.rumiapi.common.SimplePage;
import com.doddysujatmiko.rumiapi.log.LogService;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
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
    private final AnimeRepository animeRepository;

    @Autowired
    public JikanMapper(GenreRepository genreRepository, StudioRepository studioRepository, LogService logService, AnimeRepository animeRepository) {
        this.genreRepository = genreRepository;
        this.studioRepository = studioRepository;
        this.logService = logService;
        this.animeRepository = animeRepository;
    }

    public AnimeEntity toAnimeEntity(JSONObject anime) {
        List<GenreEntity> genreEntities = new ArrayList<>();
        for(var genre : anime.getJSONArray("genres")) {
            GenreEntity genreEntity = genreRepository.findByMalId(((JSONObject) genre).optInt("mal_id", 0));
            if(genreEntity == null) continue;
            genreEntities.add(genreEntity);
        }
        for(var genre : anime.getJSONArray("themes")) {
            GenreEntity genreEntity = genreRepository.findByMalId(((JSONObject) genre).optInt("mal_id", 0));
            if(genreEntity == null) continue;
            genreEntities.add(genreEntity);
        }


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
                .synopsis(StringUtils.abbreviate(anime.optString("synopsis", null), 2048))
                .image(anime.optJSONObject("images")
                        .optJSONObject("jpg")
                        .optString("image_url", null))
                .year(anime.optInt("year", 0))
                .season(anime.isNull("season") ?
                        null : SeasonEnum.valueOf(anime.optString("season").toUpperCase()))
                .genres(genreEntities)
                .studios(studioEntities)
                .hasConsumetsCache(false)
                .build();
    }

    public GenreEntity toGenreEntity(JSONObject genre) {
        return GenreEntity.builder()
                .malId(genre.optInt("mal_id", 0))
                .name(genre.optString("name"))
                .build();
    }

    public SimplePage toSimplePage(String response) {
        List<AnimeEntity> animeEntities = new ArrayList<>();

        JSONObject responseJson = new JSONObject(response);

        JSONArray animes = responseJson.getJSONArray("data");
        if(animes == null) throw new NullPointerException("Something happens and server can't get the data");

        JSONObject pagination = responseJson.getJSONObject("pagination");

        for(int i = 0; i < animes.length(); i++) {
            AnimeEntity animeEntity;
            int malId = animes.getJSONObject(i).getInt("mal_id");

            animeEntity = animeRepository.findByMalId(malId);
            if(animeEntity == null)
                animeEntity = animeRepository.save(this.toAnimeEntity(animes.getJSONObject(i)));

            animeEntities.add(animeEntity);
        }

        var simplePage = new SimplePage();
        simplePage.setMaxPage(pagination.optInt("last_visible_page", 0) - 1);
        simplePage.setCurrentPage(pagination.optInt("current_page", 0) - 1);
        simplePage.setHasNextPage(pagination.optBoolean("has_next_page", false));
        simplePage.setList(animeEntities.stream().map(AnimeDto::fromEntity).toList());

        return simplePage;
    }
}
