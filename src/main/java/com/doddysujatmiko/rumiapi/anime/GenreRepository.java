package com.doddysujatmiko.rumiapi.anime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
    GenreEntity findByMalId(Integer malId);
    GenreEntity findByName(String name);
}
