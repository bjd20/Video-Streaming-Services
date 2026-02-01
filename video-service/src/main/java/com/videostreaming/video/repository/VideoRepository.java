package com.videostreaming.video.repository;

import com.videostreaming.video.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {

    // Custom Query methods


    List<Video> findByUserId(Long userId);

    // Search videos by Title (case-insensitive)
    List<Video> findByTitleContainingIgnoreCase(String title);
}
