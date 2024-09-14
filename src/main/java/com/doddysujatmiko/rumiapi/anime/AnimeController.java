package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.common.utils.Responser;
import com.doddysujatmiko.rumiapi.consumet.enums.ServerEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/anime")
@Tag(name = "Anime")
public class AnimeController {
    private final Responser responser;
    private final AnimeService animeService;

    @Autowired
    public AnimeController(Responser responser, AnimeService animeService) {
        this.responser = responser;
        this.animeService = animeService;
    }

    @GetMapping("season/current")
    public ResponseEntity<?> getCurrentSeasonAnimes(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "25") Integer size
    ) {
            return responser.response(HttpStatus.OK, "Success", animeService.readCurrentSeasonAnimes(page, size));
    }

    @GetMapping("/genre")
    public ResponseEntity<?> getGenres() {
        return responser.response(HttpStatus.OK, "Success", animeService.readGenres());
    }

    @GetMapping("/genre/{malGenreId}")
    public ResponseEntity<?> getByGenre(
            @PathVariable Integer malGenreId) {
        return responser.response(HttpStatus.OK, "Success", animeService.readAnimesByGenre(malGenreId));
    }

    @GetMapping("/studio")
    public ResponseEntity<?> getStudios() {
        return responser.response(HttpStatus.OK, "Success", animeService.readStudios());
    }

    @GetMapping("/studio/{masStudioId}")
    public ResponseEntity<?> getByStudio(@PathVariable Integer masStudioId) {
        return responser.response(HttpStatus.OK, "Success", animeService.readAnimesByStudio(masStudioId));
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<?> getWithQuery(
            @PathVariable String query,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "25") Integer size
    ) {
        return responser.response(HttpStatus.OK, "Success", animeService.searchAnime(query, page));
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTopAnime() {
        return responser.response(HttpStatus.OK, "Success", animeService.readTopAnime());
    }

    @GetMapping("/{malAnimeId}")
    public ResponseEntity<?> getByMalId(@PathVariable Integer malAnimeId) {
        return responser.response(HttpStatus.OK, "Success", animeService.readOne(malAnimeId));
    }

    @GetMapping("/stream/{malAnimeId}")
    public ResponseEntity<?> getStreamByMalId(@PathVariable Integer malAnimeId) {
        return responser.response(HttpStatus.OK, "Success", animeService.readRelatedStreams(malAnimeId));
    }

    @GetMapping("/episode/{consumetAnimeId}")
    public ResponseEntity<?> getEpisodesByConsumetId(@PathVariable String consumetAnimeId) {
        return responser.response(HttpStatus.OK, "Success", animeService.readEpisodes(consumetAnimeId));
    }

    @GetMapping("/server/{consumetEpisodeId}")
    public ResponseEntity<?> getEpisodeServers(
            @PathVariable String consumetEpisodeId,
            @RequestParam(required = false, defaultValue = "VIDSTREAMING") ServerEnum server
            ) {
        return responser.response(HttpStatus.OK, "Success",
                animeService.readServer(consumetEpisodeId, server));
    }
}
