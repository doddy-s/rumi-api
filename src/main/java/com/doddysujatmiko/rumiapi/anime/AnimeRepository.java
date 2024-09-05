package com.doddysujatmiko.rumiapi.anime;

import com.doddysujatmiko.rumiapi.anime.enums.SeasonEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<AnimeEntity, Long> {
    AnimeEntity findByMalId(int malId);
    Boolean existsByMalId(int malId);

    Page<AnimeEntity> findByYearAndSeason(Integer year, SeasonEnum season, Pageable pageable);
}
