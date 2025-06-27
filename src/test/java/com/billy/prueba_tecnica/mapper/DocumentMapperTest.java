package com.billy.prueba_tecnica.mapper;

import com.billy.prueba_tecnica.domain.Document;
import com.billy.prueba_tecnica.dto.output.xml.XmlDocumentDto;
import com.billy.prueba_tecnica.utils.DecodificadorBase64GzipUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


class DocumentMapperTest {


    private DocumentMapper documentMapper;

    @Mock
    private DecodificadorBase64GzipUtil decodificadorBase64GzipUtil;
    private JSONObject jsonObject;
    private Document document;

    @BeforeEach
    void setUp() throws JSONException {
        MockitoAnnotations.openMocks(this);
        documentMapper= new DocumentMapper(decodificadorBase64GzipUtil);
        // Configurar JSON de prueba
        jsonObject = new JSONObject();
        jsonObject.put("id", "30000001");
        jsonObject.put("name", "Cliente 1");
        jsonObject.put("type", "FACTURA");
        jsonObject.put("totalAPagar", "7908.61"); // String en lugar de BigDecimal
        jsonObject.put("medioPago", "");

        when(decodificadorBase64GzipUtil.decode(anyString())).thenReturn(jsonObject);
        // Configurar documento de prueba
        document = Document.builder()
                .id("30000001")
                .name("Cliente 1")
                .type("FACTURA")
                .totalAPagar(new BigDecimal("7908.61"))
                .medioPago("")
                .build();
    }

    @Test
    void fromJsonStringBase64Gzip_successful() {
        // Preparar - Simular el comportamiento de DecodificadorBase64GzipUtil
        String base64Input = "H4sIACU9XGgC/6vmUlBQykxRslJQMjYAA0MlHZBYXmJuKkjUOSczNa8kVQEqXFJZABZ28vdxDXGEiuWXJOY4BiSmJxYBpcwtDSz0zAzBMrmpKZn5QIl8kBYlrloAO+/WI24AAAA=";


            // Ejecutar
            Document result = documentMapper.fromJsonStringBase64Gzip(base64Input);

            // Verificar
            assertNotNull(result);
            assertEquals("30000001", result.getId());
            assertEquals("Cliente 1", result.getName());
            assertEquals("FACTURA", result.getType());
            assertEquals(new BigDecimal("7908.61"), result.getTotalAPagar());
            assertEquals("", result.getMedioPago());

    }

    @Test
    void fromJsonStringBase64Gzip_withNullInput_returnsNull() {
        // Ejecutar
        Document result = documentMapper.fromJsonStringBase64Gzip(null);

        // Verificar
        assertNull(result);
    }

    @Test
    void fromJsonStringBase64Gzip_whenExceptionOccurs_returnsNull() {
        when(decodificadorBase64GzipUtil.decode(anyString())).thenThrow(RuntimeException.class);
        // Preparar - Simular una excepción en el decodificador
        String base64Input = "invalidBase64String";
        Document result =documentMapper.fromJsonStringBase64Gzip(base64Input);
        assertNull(result);

    }

    @Test
    void toXmlDocumentDto_successful() {
        XmlDocumentDto result = documentMapper.toXmlDocumentDto(document);

        assertNotNull(result);
        assertEquals("30000001", result.getId());
        assertEquals("Cliente 1", result.getCliente());
        assertEquals("FACTURA", result.getTipo());
        assertEquals(new BigDecimal("7908.61"), result.getTotalAPagar());
        assertEquals("", result.getMedioPago());
    }

    @Test
    void toXmlDocumentDto_withNullInput_returnsNull() {
        XmlDocumentDto result = documentMapper.toXmlDocumentDto(null);

        assertNull(result);
    }

    @Test
    void fromJsonStringBase64Gzip_withMissingFields_handlesException() throws JSONException {
        when(decodificadorBase64GzipUtil.decode(anyString())).thenReturn(null);
        // Preparar - JSON incompleto
        JSONObject incompleteJson = new JSONObject();
        incompleteJson.put("id", "123456");
        Document result = documentMapper.fromJsonStringBase64Gzip("someBase64String");
        assertNull(result);

    }
}