package com.videostreaming.video.controller;

import com.videostreaming.video.model.dto.VideoRequest;
import com.videostreaming.video.model.dto.VideoResponse;
import com.videostreaming.video.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    // Upload video
    @PostMapping
    public ResponseEntity<VideoResponse> uploadVideo(
            @Valid @RequestBody VideoRequest request,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Name") String userName) {

        VideoResponse response = videoService.uploadVideo(request, userId, userName);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all videos
    @GetMapping
    public ResponseEntity<List<VideoResponse>> getAllVideos() {
        return ResponseEntity.ok(videoService.getAllVideos());
    }

    // Get video by ID
    @GetMapping("/{id}")
    public ResponseEntity<VideoResponse> getVideoById(@PathVariable String id) {
        return ResponseEntity.ok(videoService.getVideoById(id));
    }

    // Update video
    @PutMapping("/{id}")
    public ResponseEntity<VideoResponse> updateVideo(
            @PathVariable String id,
            @RequestBody VideoRequest request) {

        return ResponseEntity.ok(videoService.updateVideo(id, request));
    }

    // Delete video
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable String id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }

    // Get videos by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VideoResponse>> getVideosByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(videoService.getVideosByUserId(userId));
    }
}
