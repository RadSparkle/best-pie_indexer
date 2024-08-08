package com.sparkle.indexer.kafka;

import com.sparkle.indexer.indexing.service.ParsingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class BestPostConsumer {
    @Autowired
    private ParsingService parsingService;

    @KafkaListener(topics = "best-post", groupId = "test")
    public void consume(Long id) {
        parsingService.parse(id);
    }
}
