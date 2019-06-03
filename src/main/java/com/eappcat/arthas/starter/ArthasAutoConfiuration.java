package com.eappcat.arthas.starter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableConfigurationProperties(ArthasConfigProperties.class)
public class ArthasAutoConfiuration {
}
