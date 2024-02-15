package com.example.jpastudy.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = [ "com.example.jpastudy.repository" ])
class AppConfig {
}