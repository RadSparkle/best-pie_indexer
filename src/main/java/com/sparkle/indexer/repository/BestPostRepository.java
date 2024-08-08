package com.sparkle.indexer.repository;

import com.sparkle.indexer.entity.BestPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BestPostRepository extends JpaRepository<BestPost, Long> {
    BestPost findBestPostById(Long id);
}
