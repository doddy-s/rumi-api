package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.anime.dtos.AnimeDto;
import com.doddysujatmiko.rumiapi.anime.dtos.GenreDto;
import com.doddysujatmiko.rumiapi.anime.dtos.StudioDto;
import com.doddysujatmiko.rumiapi.anime.enums.SeasonEnum;
import com.doddysujatmiko.rumiapi.anime.schemas.GetEpisodeSchema;
import com.doddysujatmiko.rumiapi.anime.schemas.GetServerSchema;
import com.doddysujatmiko.rumiapi.anime.schemas.GetStreamSchema;
import com.doddysujatmiko.rumiapi.common.SimplePage;
import com.doddysujatmiko.rumiapi.consumet.ConsumetAnimeRepository;
import com.doddysujatmiko.rumiapi.consumet.ConsumetEpisodeRepository;
import com.doddysujatmiko.rumiapi.consumet.ConsumetService;
import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetAnimeDto;
import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetEpisodeDto;
import com.doddysujatmiko.rumiapi.consumet.dtos.ConsumetServerDto;
import com.doddysujatmiko.rumiapi.consumet.enums.ServerEnum;
import com.doddysujatmiko.rumiapi.exceptions.NotFoundException;
import com.doddysujatmiko.rumiapi.jikan.JikanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class AnimeService {
    private final AnimeRepository animeRepository;
    private final GenreRepository genreRepository;
    private final StudioRepository studioRepository;
    private final JikanService jikanService;
    private final ConsumetService consumetService;
    private final ConsumetAnimeRepository consumetAnimeRepository;
    private final ConsumetEpisodeRepository consumetEpisodeRepository;

    @Autowired
    public AnimeService(AnimeRepository animeRepository, GenreRepository genreRepository, StudioRepository studioRepository, JikanService jikanService, ConsumetService consumetService, ConsumetAnimeRepository consumetAnimeRepository, ConsumetEpisodeRepository consumetEpisodeRepository) {
        this.animeRepository = animeRepository;
        this.genreRepository = genreRepository;
        this.studioRepository = studioRepository;
        this.jikanService = jikanService;
        this.consumetService = consumetService;
        this.consumetAnimeRepository = consumetAnimeRepository;
        this.consumetEpisodeRepository = consumetEpisodeRepository;
    }

    public Object readCurrentSeasonAnimes(Integer page) {
        var month = LocalDate.now().getMonthValue();
        SeasonEnum currentSeason = switch (month) {
            case 1, 2, 3 -> SeasonEnum.WINTER;
            case 4, 5, 6 -> SeasonEnum.SPRING;
            case 7, 8, 9 -> SeasonEnum.SUMMER;
            case 10, 11, 12 -> SeasonEnum.FALL;
            default -> throw new IllegalStateException("Unexpected value: " + month);
        };

        var animePage = animeRepository.findByYearAndSeason(LocalDate.now().getYear(),
                currentSeason,
                PageRequest.of(page, 25));

        var sp = SimplePage.<AnimeDto>fromPageWithEmptyList(animePage);
        sp.setList(animePage.getContent().stream().map(AnimeDto::fromEntity).toList());

        return sp;
    }

    public Object searchAnime(String query, Integer page) {
        return jikanService.searchAnime(query, page);
    }

    public Object readTopAnime() {
        return jikanService.readTopAnime();
    }

    public Object readGenres() {
        return genreRepository.findAll().stream().map(GenreDto::fromEntity).toList();
    }

    @Transactional
    public Object readAnimesByGenre(Integer malId) {
        var genreEntity = genreRepository.findByMalId(malId);

        if(genreEntity == null) throw new NotFoundException("Genre not found");

        return genreEntity.getAnimes().stream().map(AnimeDto::fromEntity).toList();
    }

    public Object readStudios() {
        return studioRepository.findAll().stream().map(StudioDto::fromEntity).toList();
    }

    @Transactional
    public Object readAnimesByStudio(Integer malId) {
        var studioEntity = studioRepository.findByMalId(malId);

        if(studioEntity == null) throw new NotFoundException("Studio not found");

        return studioEntity.getAnimes().stream().map(AnimeDto::fromEntity).toList();
    }

    public AnimeDto readOne(Integer malId) {
        var animeEntity = animeRepository.findByMalId(malId);

        if(animeEntity == null) animeEntity = jikanService.readOne(malId);
        if(animeEntity == null) throw new NotFoundException("Anime not found");

        return AnimeDto.fromEntity(animeEntity);
    }

    @Transactional
    public GetStreamSchema readRelatedStreams(Integer malId) {
        var animeEntity = animeRepository.findByMalId(malId);

        if(animeEntity == null) animeEntity = jikanService.readOne(malId);
        if(animeEntity == null) throw new NotFoundException("Anime not found");
        if(animeEntity.getHasConsumetsCache())
            return GetStreamSchema.builder()
                    .anime(AnimeDto.fromEntity(animeEntity))
                    .streams(animeEntity.getConsumets().stream().map(ConsumetAnimeDto::fromEntity).toList())
                    .build();

        var consumets = consumetService.readRelatedStreams(animeEntity);

        animeEntity.setConsumets(consumets);

        animeRepository.save(animeEntity);

        return GetStreamSchema.builder()
                .anime(AnimeDto.fromEntity(animeEntity))
                .streams(consumets.stream().map(ConsumetAnimeDto::fromEntity).toList())
                .build();
    }

    @Transactional
    public GetEpisodeSchema readEpisodes(String consumetId) {
        var consumetAnimeEntity = consumetAnimeRepository.findByConsumetId(consumetId);
        return GetEpisodeSchema.builder()
                .stream(ConsumetAnimeDto.fromEntity(consumetAnimeEntity))
                .episodes(consumetService.readEpisodes(consumetId)
                        .stream().map(ConsumetEpisodeDto::fromEntity).toList())
                .build();
    }

    @Transactional
    public GetServerSchema readServer(String consumetId, ServerEnum server) {
        var consumetEpisodeEntity = consumetEpisodeRepository.findByConsumetId(consumetId);
        return GetServerSchema.builder()
                .episode(ConsumetEpisodeDto.fromEntity(consumetEpisodeEntity))
                .servers(consumetService.readServers(consumetId, server)
                        .stream().map(ConsumetServerDto::fromEntity).toList())
                .build();
    }
}
