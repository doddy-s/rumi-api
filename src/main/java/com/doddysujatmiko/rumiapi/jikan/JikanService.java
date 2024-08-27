package com.doddysujatmiko.rumiapi.jikan;

import com.doddysujatmiko.rumiapi.anime.AnimeEntity;
import com.doddysujatmiko.rumiapi.anime.AnimeRepository;
import com.doddysujatmiko.rumiapi.common.SimplePage;
import com.doddysujatmiko.rumiapi.exceptions.InternalServerErrorException;
import com.doddysujatmiko.rumiapi.log.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> resultJson;
        Object data;
        Map<String, Object> pagination;
        List<AnimeEntity> animeEntities = new ArrayList<>();

        try {
            var restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(jikanApi + "/seasons/now?page=" + page + 1, String.class);
            resultJson = objectMapper.readValue(result, Map.class);
            data = resultJson.get("data");
            pagination = (Map<String, Object>) resultJson.get("pagination");
            if(data == null) throw new NullPointerException("Something happens and server can't get the data");
        } catch (Throwable t) {
            throw new InternalServerErrorException(t.getMessage());
        }

        if (data instanceof List<?>) {
            List<Map<String, Object>> animes = (List<Map<String, Object>>) data;

            for (var anime : animes) {
                AnimeEntity animeEntity;
                if(animeRepository.existsByMalId((int) anime.get("mal_id"))) {
                    animeEntity = animeRepository.findByMalId((int) anime.get("mal_id"));
                } else {
                    animeEntity = animeRepository.save(jikanMapper.toAnimeEntity(anime));
                }
                animeEntities.add(animeEntity);
            }
        }

        var res = new SimplePage();
        res.setMaxPage(pagination.get("last_visible_page") == null ? -1 : (int) pagination.get("last_visible_page") - 1);
        res.setCurrentPage(pagination.get("current_page") == null ? -1 : (int) pagination.get("current_page") - 1);
        res.setHasNextPage(pagination.get("has_next_page") == null || (boolean) pagination.get("has_next_page"));
        res.setList(animeEntities);

        return res;
    }
}
