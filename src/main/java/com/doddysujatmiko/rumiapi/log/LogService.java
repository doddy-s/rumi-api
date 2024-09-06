package com.doddysujatmiko.rumiapi.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogService {
    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void logInfo(String message) {
        log.info(message);
        saveLog(LogLevel.INFO, message);
    }

    public void logInfo(String message, Object data) {
        log.info(message, data);
        saveLog(LogLevel.INFO, message);
    }

    public void logError(String message) {
        log.error(message);
        saveLog(LogLevel.ERROR, message);
    }

    public void logError(String message, Object data) {
        log.error(message, data);
        saveLog(LogLevel.ERROR, message);
    }

    private void saveLog(LogLevel logLevel, String message) {
        var log = new LogEntity(logLevel, message);
        logRepository.save(log);
    }
}
