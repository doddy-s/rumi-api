package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.common.utils.Responser;
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
    private final JikanService jikanService;
    private final AnimeService animeService;

    @Autowired
    public AnimeController(Responser responser, JikanService jikanService, AnimeService animeService) {
        this.responser = responser;
        this.jikanService = jikanService;
        this.animeService = animeService;
    }

    @GetMapping("season/current")
    public ResponseEntity<?> getCurrentSeasonAnimes(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "false") boolean eager) {
        if(eager)
            return responser.response(HttpStatus.OK, "Success", jikanService.readCurrentSeasonAnimes(page));
        else
            return responser.response(HttpStatus.OK, "Success", animeService.readAnimes());
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
    public ResponseEntity<?> getByStudio(
            @PathVariable Integer malId) {
        return responser.response(HttpStatus.OK, "Success", animeService.readAnimesByStudio(malId));
    }

    @GetMapping("/search/{query}/{page}")
    public ResponseEntity<?> getWithQuery(
            @PathVariable String query,
            @PathVariable Integer page) {
        return responser.response(HttpStatus.OK, "Success", animeService.searchAnime(query, page));
    }

    @GetMapping("/top")
    public ResponseEntity<?> getWithQuery() {
        return responser.response(HttpStatus.OK, "Success", animeService.readTopAnime());
    }
}
