package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.common.utils.Responser;
import com.doddysujatmiko.rumiapi.consumet.enums.ProviderEnum;
import com.doddysujatmiko.rumiapi.consumet.enums.ServerEnum;
import com.doddysujatmiko.rumiapi.jikan.JikanService;
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
    public ResponseEntity<?> getCurrentSeasonAnimes(@RequestParam(required = false, defaultValue = "0") Integer page) {
            return responser.response(HttpStatus.OK, "Success", animeService.readCurrentSeasonAnimes(page));
    }

    @GetMapping("/genre")
    public ResponseEntity<?> getGenres() {
        return responser.response(HttpStatus.OK, "Success", animeService.readGenres());
    }

    @GetMapping("/genre/{malId}")
    public ResponseEntity<?> getByGenre(
            @PathVariable Integer malId) {
        return responser.response(HttpStatus.OK, "Success", animeService.readAnimesByGenre(malId));
    }

    @GetMapping("/studio")
    public ResponseEntity<?> getStudios() {
        return responser.response(HttpStatus.OK, "Success", animeService.readStudios());
    }

    @GetMapping("/studio/{malId}")
    public ResponseEntity<?> getByStudio(@PathVariable Integer malId) {
        return responser.response(HttpStatus.OK, "Success", animeService.readAnimesByStudio(malId));
    }

    @GetMapping("/search/{query}/{page}")
    public ResponseEntity<?> getWithQuery(
            @PathVariable String query,
            @PathVariable Integer page) {
        return responser.response(HttpStatus.OK, "Success", animeService.searchAnime(query, page));
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTopAnime() {
        return responser.response(HttpStatus.OK, "Success", animeService.readTopAnime());
    }

    @GetMapping("/{malId}")
    public ResponseEntity<?> getByMalId(@PathVariable Integer malId) {
        return responser.response(HttpStatus.OK, "Success", animeService.readOne(malId));
    }

    @GetMapping("/stream/{malId}")
    public ResponseEntity<?> getStreamByMalId(@PathVariable Integer malId) {
        return responser.response(HttpStatus.OK, "Success", animeService.readRelatedStreams(malId));
    }

    @GetMapping("/episodes/{consumetId}")
    public ResponseEntity<?> getEpisodesByConsumetId(@PathVariable String consumetId) {
        return responser.response(HttpStatus.OK, "Success", animeService.readEpisodes(consumetId));
    }

    @GetMapping("/episodes/{provider}/{consumetId}/")
    public ResponseEntity<?> getEpisodeServers(
            @PathVariable String consumetId,
            @PathVariable ProviderEnum provider,
            @RequestParam(required = false, defaultValue = "VIDSTREAMING") ServerEnum server
            ) {
        return responser.response(HttpStatus.OK, "Success",
                animeService.readEpisodeServers(consumetId, provider, server));
    }
}
