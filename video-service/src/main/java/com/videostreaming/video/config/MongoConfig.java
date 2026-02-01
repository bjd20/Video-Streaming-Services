package com.videostreaming.video.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    // This enables @CreatedDate, @LastModifiedDate, etc. to autopopulate fields


}
