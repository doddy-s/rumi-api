package com.doddysujatmiko.rumiapi.anime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<AnimeEntity, Long> {
    AnimeEntity findByMalId(int malId);
    Boolean existsByMalId(int malId);
}
