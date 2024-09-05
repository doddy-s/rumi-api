package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.anime.dtos.AnimeDto;
import com.doddysujatmiko.rumiapi.anime.dtos.GenreDto;
import com.doddysujatmiko.rumiapi.anime.dtos.StudioDto;
import com.doddysujatmiko.rumiapi.anime.enums.SeasonEnum;
import com.doddysujatmiko.rumiapi.common.SimplePage;
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

    @Autowired
    public AnimeService(AnimeRepository animeRepository, GenreRepository genreRepository, StudioRepository studioRepository, JikanService jikanService) {
        this.animeRepository = animeRepository;
        this.genreRepository = genreRepository;
        this.studioRepository = studioRepository;
        this.jikanService = jikanService;
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

    public Object readOne(Integer malId) {
        var animeEntity = animeRepository.findByMalId(malId);

        if(animeEntity == null) animeEntity = jikanService.readOne(malId);
        if(animeEntity == null) throw new NotFoundException("Anime not found");

        return AnimeDto.fromEntity(animeEntity);
    }
}
