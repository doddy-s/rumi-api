package com.doddysujatmiko.rumiapi.jikan;

import com.doddysujatmiko.rumiapi.anime.AnimeEntity;
import com.doddysujatmiko.rumiapi.anime.AnimeRepository;
import com.doddysujatmiko.rumiapi.common.SimplePage;
import com.doddysujatmiko.rumiapi.exceptions.InternalServerErrorException;
import com.doddysujatmiko.rumiapi.log.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class JikanService {
    @Autowired
    LogService logService;

    @Value("${jikan.api.root}")
    String jikanApi;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AnimeRepository animeRepository;

    @Autowired
    JikanMapper jikanMapper;

    public Object readCurrentSeasonAnimes(int page) {
        List<AnimeEntity> animeEntities = new ArrayList<>();
        JSONObject pagination;

        try {
            var restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(jikanApi + "/seasons/now?page=" + (page + 1), String.class);

            JSONObject responseJson = new JSONObject(response);

            JSONArray animes = responseJson.getJSONArray("data");
            if(animes == null) throw new NullPointerException("Something happens and server can't get the data");

            pagination = responseJson.getJSONObject("pagination");

            for(int i = 0; i < animes.length(); i++) {
                AnimeEntity animeEntity;
                int malId = animes.getJSONObject(i).getInt("mal_id");

                animeEntity = animeRepository.findByMalId(malId);
                if(animeEntity == null)
                    animeEntity = animeRepository.save(jikanMapper.toAnimeEntity(animes.getJSONObject(i)));

                animeEntities.add(animeEntity);
            }
        } catch (Throwable t) {
            throw new InternalServerErrorException(t.getMessage());
        }

        var res = new SimplePage();
        res.setMaxPage(pagination.optInt("last_visible_page", 0) - 1);
        res.setCurrentPage(pagination.optInt("current_page", 0) - 1);
        res.setHasNextPage(pagination.optBoolean("has_next_page", false));
        res.setList(animeEntities);

        return res;
    }
}
