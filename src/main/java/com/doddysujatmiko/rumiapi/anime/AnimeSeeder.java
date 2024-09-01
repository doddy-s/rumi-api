package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.jikan.JikanService;
import com.doddysujatmiko.rumiapi.log.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AnimeSeeder implements ApplicationRunner {
    private final JikanService jikanService;
    private final GenreRepository genreRepository;
    private final LogService logService;

    @Autowired
    public AnimeSeeder(JikanService jikanService, GenreRepository genreRepository, LogService logService) {
        this.jikanService = jikanService;
        this.genreRepository = genreRepository;
        this.logService = logService;
    }

    @Override
    public void run(ApplicationArguments args) {
        insertGenres();
    }

    void insertGenres() {
        logService.logInfo("Seeding genres table");
        if(genreRepository.count() > 0) {
            logService.logInfo("Genres already seeded -- skipping");
            return;
        }
        genreRepository.saveAll(jikanService.readAllGenres());
    }
}
