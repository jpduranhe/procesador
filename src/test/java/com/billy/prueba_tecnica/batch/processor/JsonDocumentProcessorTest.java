package com.billy.prueba_tecnica.batch.processor;

import com.billy.prueba_tecnica.domain.Document;
import com.billy.prueba_tecnica.dto.input.json.JsonDocumentDto;
import com.billy.prueba_tecnica.mapper.DocumentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class JsonDocumentProcessorTest {
    @Mock
    private DocumentMapper documentMapper;


    private JsonDocumentProcessor processor;

    private JsonDocumentDto inputDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        processor= new JsonDocumentProcessor(documentMapper);
        // Crear el DTO de entrada
        inputDto = JsonDocumentDto.builder().ContentBase64("H4sIACU9XGgC/6vmUlBQykxRslJQMjYAA0MlHZBYXmJuKkjUOSczNa8kVQEqXFJZABZ28vdxDXGEiuWXJOY4BiSmJxYBpcwtDSz0zAzBMrmpKZn5QIl8kBYlrloAO+/WI24AAAA=").build();

    }

    @Test
    void process_shouldCallMapperAndReturnDocument() throws Exception {
        // Configuración del mock
        when(documentMapper.fromJsonStringBase64Gzip(anyString()))
                .thenReturn(Document.builder().build());

        // Ejecutar el método a probar
        Document result = processor.process(inputDto);

        // Verificar resultados
        assertNotNull(result);

        // Verificar que el mapper fue llamado correctamente
        verify(documentMapper, times(1)).fromJsonStringBase64Gzip(anyString());
    }
}