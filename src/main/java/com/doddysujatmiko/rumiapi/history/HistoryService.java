package com.doddysujatmiko.rumiapi.history;

import com.doddysujatmiko.rumiapi.auth.UserEntity;
import com.doddysujatmiko.rumiapi.auth.UserRepository;
import com.doddysujatmiko.rumiapi.auth.dtos.UserDto;
import com.doddysujatmiko.rumiapi.common.SimplePage;
import com.doddysujatmiko.rumiapi.consumet.ConsumetAnimeRepository;
import com.doddysujatmiko.rumiapi.consumet.ConsumetEpisodeRepository;
import com.doddysujatmiko.rumiapi.exceptions.NotFoundException;
import com.doddysujatmiko.rumiapi.history.dtos.HistoryDto;
import com.doddysujatmiko.rumiapi.history.dtos.HistoryWithConsumetEpisodeDtoDto;
import com.doddysujatmiko.rumiapi.history.dtos.PostHistoryReqDto;
import com.doddysujatmiko.rumiapi.history.schemas.GetHistorySchema;
import com.doddysujatmiko.rumiapi.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
public class HistoryService {
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final ValidationService validationService;
    private final ConsumetEpisodeRepository consumetEpisodeRepository;

    @Autowired
    public HistoryService(UserRepository userRepository, HistoryRepository historyRepository, ValidationService validationService, ConsumetAnimeRepository consumetAnimeRepository, ConsumetEpisodeRepository consumetEpisodeRepository) {
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
        this.validationService = validationService;
        this.consumetEpisodeRepository = consumetEpisodeRepository;
    }

    private GetHistorySchema readHistory(UserEntity user, Integer page, Integer size) {
        var animePage = historyRepository.findByUser(user, PageRequest.of(page, size));

        if(animePage.getNumberOfElements() < 1) throw new NotFoundException("This user dont have history");

        var sp = SimplePage.fromPage(animePage);

        return GetHistorySchema.builder()
                .user(UserDto.fromEntity(user))
                .historyPage(SimplePage.changeListType(sp, HistoryWithConsumetEpisodeDtoDto::fromEntity))
                .build();
    }

    @Transactional
    public GetHistorySchema readAuthenticatedUserHistory(Principal principal, Integer page, Integer size) {
        var user = userRepository.findByUsername(principal.getName());
        return readHistory(user, page, size);
    }

    @Transactional
    public GetHistorySchema readSomeoneHistory(String username, Integer page, Integer size) {
        var user = userRepository.findByUsername(username);
        return readHistory(user, page, size);
    }

    @Transactional
    public HistoryDto createHistory(Principal principal, PostHistoryReqDto req) {
        validationService.validate(req);

        var user = userRepository.findByUsername(principal.getName());
        var episode = consumetEpisodeRepository.findByConsumetId(req.getConsumetEpisodeId());

        if(episode == null) throw new NotFoundException("no such episode");

        var optionalHistoryEntity =  user.getHistories().stream()
                .filter((historyEntity -> historyEntity.getConsumetAnime().equals(episode.getConsumetAnime())))
                .findFirst();

        HistoryEntity history;

        if(optionalHistoryEntity.isPresent()) {
            history = optionalHistoryEntity.get();
            history.setEpisodeNumber(episode.getNumber());
            history.setSecond(req.getSecond());
        } else {
            history = HistoryEntity.builder()
                    .user(user)
                    .consumetAnime(episode.getConsumetAnime())
                    .episodeNumber(episode.getNumber())
                    .second(req.getSecond())
                    .build();
        }

        return HistoryDto.fromEntity(historyRepository.save(history));
    }
}
