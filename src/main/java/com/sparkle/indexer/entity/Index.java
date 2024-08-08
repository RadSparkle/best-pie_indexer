package com.sparkle.indexer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.List;

@Document(indexName = "best-post")
@Getter
@Setter
@NoArgsConstructor
public class Index {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "site_name")
    @Field("sitename")
    private String siteName;

    @Column(name = "title")
    @Field("title")
    private String title;

    @Field(name = "content")
    private String content;

    @Field("keywords")
    private List<String> keywords;

    @Field("url")
    private String url;

    @Column(name = "scraped_at")
    @Field(name = "scraped_at")
    private String scraped_at;
}
