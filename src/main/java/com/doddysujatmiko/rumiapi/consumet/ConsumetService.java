package com.doddysujatmiko.rumiapi.consumet;

import com.doddysujatmiko.rumiapi.anime.GenreEntity;
import com.doddysujatmiko.rumiapi.anime.enums.SeasonEnum;
import com.doddysujatmiko.rumiapi.common.SimplePage;
import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetDto;
import com.doddysujatmiko.rumiapi.consumet.enums.ProviderEnum;
import com.doddysujatmiko.rumiapi.exceptions.InternalServerErrorException;
import com.doddysujatmiko.rumiapi.log.LogService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ConsumetService {
    private final ConsumetMapper consumetMapper;
    private final ConsumetRepository consumetRepository;
    private final LogService logService;

    @Value("${CONSUMET_API_HOST}")
    String consumetApi;

    @Autowired
    public ConsumetService(ConsumetMapper consumetMapper, ConsumetRepository consumetRepository, LogService logService) {
        this.consumetMapper = consumetMapper;
        this.consumetRepository = consumetRepository;
        this.logService = logService;
    }

    public List<ConsumetEntity> readRelatedStreams(String query) {
        var hianime = search(query, ProviderEnum.HIANIME);
        var gogoanime = search(query, ProviderEnum.GOGOANIME);

        List<ConsumetEntity> consumetEntities = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            if(i < hianime.size()) consumetEntities.add(hianime.get(i));
            if(i < gogoanime.size()) consumetEntities.add(gogoanime.get(i));
        }

        return consumetEntities;
    }

    private List<ConsumetEntity> search(String query, ProviderEnum provider) {
        List<ConsumetEntity> consumetEntities = new ArrayList<>();

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
                var consumet = consumetRepository.findByConsumetId(streams.getJSONObject(i).getString("id"));
                if(consumet == null)
                    consumet = consumetRepository.save(
                            consumetMapper.toConsumetEntity(streams.getJSONObject(i), provider));
                consumetEntities.add(consumet);
            }
        } catch (Throwable t) {
            throw new InternalServerErrorException(t.getMessage());
        }

        return consumetEntities;
    }
}
