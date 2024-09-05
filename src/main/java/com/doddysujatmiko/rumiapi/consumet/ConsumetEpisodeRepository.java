package com.doddysujatmiko.rumiapi.consumet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumetEpisodeRepository extends JpaRepository<ConsumetEpisodeEntity, Long> {
    ConsumetEpisodeEntity findByConsumetId(String consumetId);
}
