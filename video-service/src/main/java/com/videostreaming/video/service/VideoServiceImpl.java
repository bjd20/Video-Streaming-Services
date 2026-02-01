package com.videostreaming.video.service;

import com.videostreaming.video.exception.VideoNotFoundException;
import com.videostreaming.video.model.Video;
import com.videostreaming.video.model.dto.VideoRequest;
import com.videostreaming.video.model.dto.VideoResponse;
import com.videostreaming.video.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;


    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public VideoResponse uploadVideo(VideoRequest request, Long userId, String uploaderName) {
        Video video = toEntity(request, userId, uploaderName);
        videoRepository.save(video);
        return toResponse(video);
    }

    @Override
    public List<VideoResponse> getAllVideos() {
        return videoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public VideoResponse getVideoById(String id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video with ID "+id+ " not found"));

        return toResponse(video);
    }

    @Override
    public VideoResponse updateVideo(String id, VideoRequest request) {
        Video updated = videoRepository.findById(id)
                .map(v -> {
                    // Only update if request field is not null
                    if (request.getTitle() != null) {
                        v.setTitle(request.getTitle());
                    }
                    if (request.getDescription() != null) {
                        v.setDescription(request.getDescription());
                    }
                    if (request.getDuration() != null) {
                        v.setDuration(request.getDuration());
                    }
                    if (request.getThumbnailUrl() != null) {
                        v.setThumbnailUrl(request.getThumbnailUrl());
                    }
                    if (request.getVideoUrl() != null) {
                        v.setVideoUrl(request.getVideoUrl());
                    }

                    return videoRepository.save(v);
                })
                .orElseThrow(() -> new VideoNotFoundException("Video with ID "+id+ " not found"));

        return toResponse(updated);
    }

    @Override
    public void deleteVideo(String id) {
        if(!videoRepository.existsById(id))
            throw new VideoNotFoundException("Video with ID "+id+ " not found");

        videoRepository.deleteById(id);
    }

    @Override
    public List<VideoResponse> getVideosByUserId(Long userId) {
        return videoRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    private VideoResponse toResponse(Video video){
        return new VideoResponse(
                video.getId(),
                video.getTitle(),
                video.getDescription(),
                video.getDuration(),
                video.getUploadDate(),
                video.getUserId(),
                video.getUploaderName(),
                video.getThumbnailUrl(),
                video.getVideoUrl(),
                video.getViewCount());
        }

    private Video toEntity(VideoRequest request, Long userId,  String uploaderName){
        return new Video(
            request.getTitle(),
            request.getDescription(),
            request.getDuration(),
            userId,
            uploaderName,
            request.getThumbnailUrl(),
            request.getVideoUrl()
        );
    }
}
