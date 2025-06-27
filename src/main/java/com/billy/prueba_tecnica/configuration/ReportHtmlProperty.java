package com.billy.prueba_tecnica.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "report-html")
@Data
public class ReportHtmlProperty {
    private String path;

}
