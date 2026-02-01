package com.videostreaming.video.model.dto;

import java.time.LocalDateTime;

public class VideoResponse {

    private String id;
    private String title;
    private String description;
    private Integer duration;
    private LocalDateTime uploadDate;
    private Long userId;
    private String uploaderName;
    private String thumbnailUrl;
    private String videoUrl;
    private Integer viewCount;

    public VideoResponse() {
    }

    public VideoResponse(String id, String title, String description, Integer duration,
                         LocalDateTime uploadDate, Long userId, String uploaderName, String thumbnailUrl,
                         String videoUrl, Integer viewCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.uploadDate = uploadDate;
        this.userId = userId;
        this.uploaderName = uploaderName;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.viewCount = viewCount;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
}
