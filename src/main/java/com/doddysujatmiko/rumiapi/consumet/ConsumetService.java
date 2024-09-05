package com.doddysujatmiko.rumiapi.consumet;

import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetEpisodeDto;
import com.doddysujatmiko.rumiapi.consumet.enums.ProviderEnum;
import com.doddysujatmiko.rumiapi.exceptions.InternalServerErrorException;
import com.doddysujatmiko.rumiapi.exceptions.NotFoundException;
import com.doddysujatmiko.rumiapi.log.LogService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsumetService {
    private final ConsumetMapper consumetMapper;
    private final ConsumetAnimeRepository consumetAnimeRepository;
    private final LogService logService;
    private final ConsumetEpisodeRepository consumetEpisodeRepository;

    @Value("${CONSUMET_API_HOST}")
    String consumetApi;

    @Autowired
    public ConsumetService(ConsumetMapper consumetMapper, ConsumetAnimeRepository consumetAnimeRepository, LogService logService, ConsumetEpisodeRepository consumetEpisodeRepository) {
        this.consumetMapper = consumetMapper;
        this.consumetAnimeRepository = consumetAnimeRepository;
        this.logService = logService;
        this.consumetEpisodeRepository = consumetEpisodeRepository;
    }

    public List<ConsumetAnimeEntity> readRelatedStreams(String query) {
        var hianime = search(query, ProviderEnum.HIANIME);
        var gogoanime = search(query, ProviderEnum.GOGOANIME);

        List<ConsumetAnimeEntity> consumetEntities = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            if(i < hianime.size()) consumetEntities.add(hianime.get(i));
            if(i < gogoanime.size()) consumetEntities.add(gogoanime.get(i));
        }

        return consumetEntities;
    }

    private List<ConsumetAnimeEntity> search(String query, ProviderEnum provider) {
        List<ConsumetAnimeEntity> consumetEntities = new ArrayList<>();

        query = query.replaceAll("[^a-zA-Z0-9 ]", "");

        logService.logInfo("Searching "+query+" on "+provider);

        try {
            var restTemplate = new RestTemplate();

            String branch = switch (provider) {
                case ProviderEnum.HIANIME -> "zoro";
                case ProviderEnum.GOGOANIME -> "gogoanime";
            };
            String url = consumetApi + "/anime/" + branch + "/" + query;
            String response = restTemplate.getForObject(url, String.class);

            JSONObject responseJson = new JSONObject(response);

            JSONArray streams = responseJson.getJSONArray("results");

            for(int i = 0; i < streams.length(); i++) {
                var consumet = consumetAnimeRepository.findByConsumetId(streams.getJSONObject(i).getString("id"));
                if(consumet == null)
                    consumet = consumetAnimeRepository.save(
                            consumetMapper.toConsumetEntity(streams.getJSONObject(i), provider));
                consumetEntities.add(consumet);
            }
        } catch (Throwable t) {
            throw new InternalServerErrorException(t.getMessage());
        }

        return consumetEntities;
    }

    public List<ConsumetEpisodeDto> readEpisodes(String consumetId) {
        List<ConsumetEpisodeEntity> episodeEntities = new ArrayList<>();

        try {
            var consumet = consumetAnimeRepository.findByConsumetId(consumetId);

            if(consumet == null) throw new NotFoundException("Consumet not found");

            var restTemplate = new RestTemplate();

            String url = switch (consumet.getProvider()) {
                case ProviderEnum.HIANIME -> consumetApi + "/anime/zoro/info?id=" + consumetId;
                case ProviderEnum.GOGOANIME -> consumetApi + "/anime/gogoanime/info/" + consumetId;
            };
            String response = restTemplate.getForObject(url, String.class);

            JSONObject responseJson = new JSONObject(response);

            JSONArray episodes = responseJson.getJSONArray("episodes");

            for(int i = 0; i < episodes.length(); i++) {
                var episode = consumetEpisodeRepository
                        .findByConsumetId(episodes.getJSONObject(i).getString("id"));
                if(episode == null)
                    episode = consumetEpisodeRepository
                            .save(consumetMapper.toConsumetEpisodeEntity(episodes.getJSONObject(i), consumet));
                episodeEntities.add(episode);
            }


        } catch (Throwable t) {
            throw new InternalServerErrorException(t.getMessage());
        }

        return episodeEntities.stream().map(ConsumetEpisodeDto::fromEntity).toList();
    }
}
