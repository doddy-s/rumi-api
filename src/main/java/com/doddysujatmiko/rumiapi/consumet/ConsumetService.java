package com.doddysujatmiko.rumiapi.consumet;

import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetEpisodeDto;
import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetServerDto;
import com.doddysujatmiko.rumiapi.consumet.enums.ProviderEnum;
import com.doddysujatmiko.rumiapi.consumet.enums.ServerEnum;
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
    private final ConsumetServerRepository consumetServerRepository;

    @Value("${CONSUMET_API_HOST}")
    String consumetApi;

    @Autowired
    public ConsumetService(ConsumetMapper consumetMapper, ConsumetAnimeRepository consumetAnimeRepository, LogService logService, ConsumetEpisodeRepository consumetEpisodeRepository, ConsumetServerRepository consumetServerRepository) {
        this.consumetMapper = consumetMapper;
        this.consumetAnimeRepository = consumetAnimeRepository;
        this.logService = logService;
        this.consumetEpisodeRepository = consumetEpisodeRepository;
        this.consumetServerRepository = consumetServerRepository;
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
            logService.logError("Error consumet search on provider "+provider+" with message "+t.getMessage());
            return new ArrayList<>();
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

    public List<ConsumetServerDto> readEpisodeServers(String consumetId, ProviderEnum provider, ServerEnum server) {
        List<ConsumetServerEntity> serverEntities = new ArrayList<>();

        try {
            var episode = consumetEpisodeRepository.findByConsumetId(consumetId);

            if(episode == null) throw new NotFoundException("Consumet episode not found");

            var restTemplate = new RestTemplate();

            String branch = switch (provider) {
                case ProviderEnum.HIANIME -> "zoro";
                case ProviderEnum.GOGOANIME -> "gogoanime";
            };
            String response = restTemplate.getForObject(
                    consumetApi + "/anime/" + branch + "/watch/" + consumetId +
                        "?server=" + server.name().toLowerCase(),
                    String.class);

            JSONObject responseJson = new JSONObject(response);

            JSONArray sources = responseJson.getJSONArray("sources");

            for(int i = 0; i < sources.length(); i++) {
                var source = consumetServerRepository
                        .findByUrl(sources.getJSONObject(i).optString("url", null));
                if(source == null)
                    source = consumetServerRepository
                            .save(consumetMapper.toConsumetServerEntity(sources.getJSONObject(i), server, episode));
                serverEntities.add(source);
            }


        } catch (Throwable t) {
            throw new InternalServerErrorException(t.getMessage());
        }

        return serverEntities.stream().map(ConsumetServerDto::fromEntity).toList();
    }
}
