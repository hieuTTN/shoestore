package com.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {"com.web.repository"})
@EnableElasticsearchRepositories(basePackages = {"com.web.elasticsearch.repository"})
@EnableTransactionManagement
public class DatabaseConfiguration {
}

