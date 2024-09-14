package com.doddysujatmiko.rumiapi.jikan;

import com.doddysujatmiko.rumiapi.anime.AnimeEntity;
import com.doddysujatmiko.rumiapi.anime.AnimeRepository;
import com.doddysujatmiko.rumiapi.anime.GenreEntity;
import com.doddysujatmiko.rumiapi.common.SimplePage;
import com.doddysujatmiko.rumiapi.exceptions.InternalServerErrorException;
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
    private final JikanMapper jikanMapper;
    private final AnimeRepository animeRepository;

    @Autowired
    public JikanService(AnimeRepository animeRepository, JikanMapper jikanMapper) {
        this.animeRepository = animeRepository;
        this.jikanMapper = jikanMapper;
    }

    @Value("${jikan.api.root}")
    String jikanApi;

    public SimplePage<AnimeEntity> readCurrentSeasonAnimes(int page) {
        SimplePage<AnimeEntity> simplePage;

        try {
            var restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(jikanApi + "/seasons/now?page=" + (page + 1), String.class);

            simplePage = jikanMapper.toSimplePage(response);
        } catch (Throwable t) {
            throw new InternalServerErrorException(t.getMessage());
        }

        return simplePage;
    }

    public List<GenreEntity> readAllGenres() {
        List<GenreEntity> genreEntities = new ArrayList<>();

        try {
            var restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(jikanApi + "/genres/anime", String.class);

            JSONObject responseJson = new JSONObject(response);

            JSONArray genres = responseJson.getJSONArray("data");
            if(genres == null) throw new NullPointerException("Something happens and server can't get the data");

            for(int i = 0; i < genres.length(); i++)
                genreEntities.add(jikanMapper.toGenreEntity(genres.getJSONObject(i)));
        } catch (Throwable t) {
            throw new InternalServerErrorException(t.getMessage());
        }

        return genreEntities;
    }

    public SimplePage<AnimeEntity> readAllAnimes(Integer page) {
        SimplePage<AnimeEntity> simplePage;

        try {
            var restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(jikanApi + "/anime?page=" + (page + 1), String.class);

            simplePage = jikanMapper.toSimplePage(response);
        } catch (Throwable t) {
            throw new InternalServerErrorException(t.getMessage());
        }

        return simplePage;
    }

    public SimplePage<AnimeEntity> searchAnime(String query, Integer page) {
        SimplePage<AnimeEntity> simplePage;

        try {
            var restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(jikanApi +
                    "/anime?page=" + (page + 1) +
                    "&q=" + query, String.class);

            simplePage = jikanMapper.toSimplePage(response);
        } catch (Throwable t) {
            throw new InternalServerErrorException(t.getMessage());
        }

        return simplePage;
    }

    public SimplePage<AnimeEntity> readTopAnime() {
        SimplePage<AnimeEntity> simplePage;

        try {
            var restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(jikanApi +
                    "/top/anime?filter=airing&type=tv", String.class);

            simplePage = jikanMapper.toSimplePage(response);
        } catch (Throwable t) {
            throw new InternalServerErrorException(t.getMessage());
        }

        return simplePage;
    }

    public AnimeEntity readOne(Integer malId) {
        try {
            var restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(jikanApi +
                    "/anime/"+malId, String.class);

            return animeRepository.save(jikanMapper.toAnimeEntity(new JSONObject(response).getJSONObject("data")));
        } catch (Throwable t) {
            return null;
        }
    }
}
