package com.doddysujatmiko.rumiapi.history;

import com.doddysujatmiko.rumiapi.auth.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {
    Page<HistoryEntity> findByUser(UserEntity user, Pageable pageable);
}
