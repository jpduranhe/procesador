package com.billy.prueba_tecnica.mapper;

import com.billy.prueba_tecnica.dto.input.json.JsonDocumentContainerDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class JsonDocumentDtoContainerMapperTest {

    private DocumentContainerMapper documentContainerMapper;


    @BeforeEach
    void setUp() {

        ObjectMapper objectMapper = new ObjectMapper();
        documentContainerMapper = new DocumentContainerMapper(objectMapper);

    }

    @Test
    void fromFileJson_shouldReturnDocumentContainer_whenFileExists() {
        URL resourceUrl = getClass().getClassLoader().getResource("file.json");
        JsonDocumentContainerDto result = documentContainerMapper.fromFileJson(resourceUrl.getPath());
        // Assert
        assertNotNull(result);
        assertNotNull(result.documents());
        assertEquals(3, result.documents().size());
    }



    @Test
    void fromFileJson_shouldThrowRuntimeException_whenJsonIsInvalid() {

        assertThrows(RuntimeException.class, () -> {
            documentContainerMapper.fromFileJson("invalid_path.json");
        });
    }

}