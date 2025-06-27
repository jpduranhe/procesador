package com.billy.prueba_tecnica.batch.reader;

import com.billy.prueba_tecnica.dto.input.json.JsonDocumentContainerDto;
import com.billy.prueba_tecnica.dto.input.json.JsonDocumentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DocumentContainerJsonReaderTest {

    private DocumentContainerJsonReader reader;


    private ObjectMapper mockMapper;

    private JsonDocumentDto doc1;
    private JsonDocumentDto doc2;
    private JsonDocumentContainerDto container;

    @BeforeEach
    void setUp() {
        mockMapper = new ObjectMapper();
        reader = new DocumentContainerJsonReader();

        // Crear documentos de prueba
        doc1 = JsonDocumentDto.builder()
                .ContentBase64("content1")
                .build();

        doc2 = JsonDocumentDto.builder()
                .ContentBase64("content2")
                .build();

        // Crear contenedor
        container = new JsonDocumentContainerDto(Arrays.asList(doc1, doc2));
    }

    @Test
    void constructor_withNullMapper_shouldThrowException() {
        // Verificar que el constructor con mapper nulo lanza excepción
        assertThrows(IllegalArgumentException.class, () -> new DocumentContainerJsonReader(null));
    }

    @Test
    void setMapper_withNullMapper_shouldThrowException() {
        // Verificar que setMapper con nulo lanza excepción
        assertThrows(IllegalArgumentException.class, () -> reader.setMapper(null));
    }

    @Test
    void open_withNullResource_shouldThrowException() {
        // Verificar que open con recurso nulo lanza excepción
        assertThrows(IllegalArgumentException.class, () -> reader.open(null));
    }



    @Test
    void open_withEmptyContainer_shouldInitializeEmptyList() throws Exception {
        // Preparar
        String json = "{\"documents\":[]}";
        Resource resource = new ByteArrayResource(json.getBytes());

        // Usar el ObjectMapper real
        reader = new DocumentContainerJsonReader(new ObjectMapper());

        // Ejecutar
        reader.open(resource);

        // Verificar que no hay documentos
        assertNull(reader.read());
    }





}