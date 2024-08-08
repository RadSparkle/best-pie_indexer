package com.sparkle.indexer.indexing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparkle.indexer.entity.Index;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class IndexingService {
    @Autowired
    private RestHighLevelClient client;

    public void index(Index bestPost) {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> sourceAsMap;
        try {
            sourceAsMap = objectMapper.convertValue(bestPost, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        IndexRequest indexRequest = new IndexRequest("best-post")
                .id(bestPost.getId().toString())
                .source(sourceAsMap, XContentType.JSON);

        try {
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Indexing completed | {} | {}", bestPost.getSiteName(), bestPost.getTitle());
    }
}
