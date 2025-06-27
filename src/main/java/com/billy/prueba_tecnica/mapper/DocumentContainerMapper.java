package com.billy.prueba_tecnica.mapper;

import com.billy.prueba_tecnica.dto.input.json.JsonDocumentContainerDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class DocumentContainerMapper {

    private final ObjectMapper objectMapper;

    public JsonDocumentContainerDto fromFileJson(String filePath) {
        try {
            File file = new File(filePath);
            return objectMapper.readValue(file, JsonDocumentContainerDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + filePath, e);
        }

    }
}
