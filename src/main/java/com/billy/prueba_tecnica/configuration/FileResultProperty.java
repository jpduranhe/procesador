package com.billy.prueba_tecnica.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file-result")
@Data
public class FileResultProperty {
    private String path;

}
