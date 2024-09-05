package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.anime.dtos.AnimeDto;
import com.doddysujatmiko.rumiapi.anime.dtos.GenreDto;
import com.doddysujatmiko.rumiapi.anime.dtos.StudioDto;
import com.doddysujatmiko.rumiapi.common.SimplePage;
import com.doddysujatmiko.rumiapi.exceptions.NotFoundException;
import com.doddysujatmiko.rumiapi.jikan.JikanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Object readAnimes() {
        var res = new SimplePage();
        res.setMaxPage(0);
        res.setCurrentPage(0);
        res.setHasNextPage(false);
        res.setList(animeRepository.findAll().stream().map(AnimeDto::fromEntity).toList());

        return res;
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
