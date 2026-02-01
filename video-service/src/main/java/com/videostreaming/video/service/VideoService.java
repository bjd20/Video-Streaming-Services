package com.videostreaming.video.service;

import com.videostreaming.video.model.dto.VideoRequest;
import com.videostreaming.video.model.dto.VideoResponse;

import java.util.List;

public interface VideoService {

    VideoResponse uploadVideo(VideoRequest request, Long userId, String uploaderName);

    List<VideoResponse> getAllVideos();

    VideoResponse getVideoById(String id);

    VideoResponse updateVideo(String id, VideoRequest request);

    void deleteVideo(String id);

    List<VideoResponse> getVideosByUserId(Long userId);




}
