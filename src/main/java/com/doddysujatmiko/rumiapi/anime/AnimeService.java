package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.common.SimplePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimeService {
    @Autowired
    AnimeRepository animeRepository;

    public Object readAnimes() {
        var res = new SimplePage();
        res.setMaxPage(0);
        res.setCurrentPage(0);
        res.setHasNextPage(false);
        res.setList(animeRepository.findAll());

        return res;
    }
}
