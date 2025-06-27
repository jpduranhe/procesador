package com.billy.prueba_tecnica.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file-processing")
@Data
public class FileProcessingProperty {


    private String name;
    private String path;
    private int chunkSize;


    public String getFileInProcessingPath() {
        return path + name;
    }
}
