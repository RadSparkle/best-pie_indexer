package com.sparkle.indexer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication
@ComponentScan(basePackages = "com.sparkle")
public class BestPieIndexerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BestPieIndexerApplication.class, args);
    }

}
