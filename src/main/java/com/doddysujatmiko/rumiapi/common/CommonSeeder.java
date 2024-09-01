package com.doddysujatmiko.rumiapi.common;

import com.doddysujatmiko.rumiapi.log.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CommonSeeder implements ApplicationRunner {
    private final CommonRepository commonRepository;
    private final LogService logService;

    @Autowired
    public CommonSeeder(CommonRepository commonRepository, LogService logService) {
        this.commonRepository = commonRepository;
        this.logService = logService;
    }

    @Override
    public void run(ApplicationArguments args) {
        insertCommon();
    }

    private void insertCommon() {
        logService.logInfo("Seeding commons table");
        commonRepository.save(CommonEntity.builder().key("JikanScrapeCurrentPage").value(0).build());
        commonRepository.save(CommonEntity.builder().key("JikanScrapeMaxPage").value(10).build());
    }
}
