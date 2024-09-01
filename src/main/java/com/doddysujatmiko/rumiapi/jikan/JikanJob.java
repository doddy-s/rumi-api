package com.doddysujatmiko.rumiapi.jikan;

import com.doddysujatmiko.rumiapi.common.CommonRepository;
import com.doddysujatmiko.rumiapi.log.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableScheduling
public class JikanJob {
    private final LogService logService;
    private final CommonRepository commonRepository;
    private final JikanService jikanService;

    @Autowired
    public JikanJob(LogService logService, CommonRepository commonRepository, JikanService jikanService) {
        this.logService = logService;
        this.commonRepository = commonRepository;
        this.jikanService = jikanService;
    }

    @Scheduled(cron = "*/20 * * * * *")
    public void scrapeJikanAnimes() {
        var currentPage = commonRepository.findByKey("JikanScrapeCurrentPage");
        var maxPage = commonRepository.findByKey("JikanScrapeMaxPage");

        if(currentPage.getValue() > maxPage.getValue()) {
            logService.logInfo("Scraping jikan completed on page "
                    + maxPage.getValue()
                    + " at " + new Date()
                    + " --- aborting");
            return;
        }

        logService.logInfo("Scraping jikan on page " + currentPage.getValue() + " at " + new Date());
        var animes = jikanService.readAllAnimes(currentPage.getValue());

        currentPage.setValue(currentPage.getValue() + 1);
        commonRepository.save(currentPage);

        maxPage.setValue(animes.getMaxPage());
        commonRepository.save(maxPage);
    }
}
