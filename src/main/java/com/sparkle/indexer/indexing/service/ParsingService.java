package com.sparkle.indexer.indexing.service;

import com.sparkle.indexer.entity.BestPost;
import com.sparkle.indexer.entity.Index;
import com.sparkle.indexer.entity.TokenResponse;
import com.sparkle.indexer.repository.BestPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class ParsingService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private IndexingService indexingService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final BestPostRepository bestPostRepository;

    public void parse(Long id) {
        try {
            BestPost bestPost = bestPostRepository.findBestPostById(id);
            String content = null;
            int maxRetries = 3;
            int retryCount = 0;
            int retryDelay = 2000;

            while (retryCount < maxRetries) {
                try {
                    content = redisTemplate.opsForValue().get(id.toString());
                    redisTemplate.delete(id.toString());
                    break; // 성공 시 루프 탈출
                } catch (Exception e) {
                    retryCount++;
                    log.error("Redis 연결 오류:{}, 재시도:{} / {}, ", e.getMessage(), retryCount, maxRetries);

                    if (retryCount >= maxRetries) {
                        log.error("최대 재시도 횟수 초과. 기본값으로 재진행");
                        content = ""; // 기본값 설정
                    } else {
                        try {
                            Thread.sleep(retryDelay); // 대기 후 재시도
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt(); // 인터럽트 상태 복원
                            throw new RuntimeException("재시도 대기 중 인터럽트 발생", ie);
                        }
                    }
                }
            }

            Index index = new Index();
            index.setId(id);
            index.setTitle(bestPost.getTitle());
            index.setSiteName(bestPost.getSiteName());
            index.setKeywords(getKeywords(bestPost.getTitle() + content));
            index.setUrl(bestPost.getUrl());
            index.setContent(content);
            index.setScraped_at(bestPost.getScrapedAt());

            indexingService.index(index);
        } catch (NullPointerException e) {
            log.error("Not exist id in DB: {}", id);
        }
    }

    public List<String> getKeywords(String content) {
        Set<String> set = new HashSet<>();

        WebClient webClient = webClientBuilder.build();
        String url = "http://127.0.0.1:5000/analyze?content=" + content;
        TokenResponse response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();

        if (response != null) {
            List<String> tokens = response.getTokens();
            for (String token : tokens) {
                set.add(token);
            }
        }

        List<String> keywords = new ArrayList<>(set);
        return keywords;
    }
}
