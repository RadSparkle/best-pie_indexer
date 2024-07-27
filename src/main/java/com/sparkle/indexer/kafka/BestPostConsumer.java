package com.sparkle.indexer.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BestPostConsumer {
    @KafkaListener(topics = "best-post", groupId = "test")
    public void consume(Long id) {

    }
}
