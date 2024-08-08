package com.sparkle.indexer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

@Entity
@Table(name = "best_post", uniqueConstraints = {@UniqueConstraint(columnNames = {"site_name", "title"})})
@Getter @Setter @NoArgsConstructor
public class BestPost implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "site_name")
    @Field("sitename")
    private String siteName;

    @Column(name = "title")
    @Field("title")
    private String title;

    @Column(name = "url")
    @Field("url")
    private String url;

    @Column(name = "scraped_at")
    private String scrapedAt;
}
