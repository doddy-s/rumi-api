package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.common.utils.Responser;
import com.doddysujatmiko.rumiapi.jikan.JikanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anime")
public class AnimeController {
    @Autowired
    Responser responser;

    @Autowired
    JikanService jikanService;

    @Autowired
    AnimeService animeService;

    @GetMapping("season/current")
    public ResponseEntity<?> getCurrentSeasonAnimes(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "false") boolean eager) {
        if(eager)
            return responser.response(HttpStatus.OK, "Success", jikanService.readCurrentSeasonAnimes(page));
        else
            return responser.response(HttpStatus.OK, "Success", animeService.readAnimes());
    }
}
