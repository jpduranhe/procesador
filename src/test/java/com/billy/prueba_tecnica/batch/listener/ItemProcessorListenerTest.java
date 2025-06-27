package com.billy.prueba_tecnica.batch.listener;

import com.billy.prueba_tecnica.domain.Document;
import com.billy.prueba_tecnica.dto.input.json.JsonDocumentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.DoubleAccumulator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ItemProcessorListenerTest {

    @Spy
    private ConcurrentMap<String, DoubleAccumulator> dataForHml = new ConcurrentHashMap<>();

    @InjectMocks
    private ItemProcessorListener listener;

    private JsonDocumentDto inputDto;
    private Document outputDocument;

    @BeforeEach
    void setUp() {
        inputDto =  JsonDocumentDto.builder().build();

        outputDocument =  Document.builder()
                .totalAPagar(new BigDecimal("1500.75"))
                .build();
    }

    @Test
    void afterProcess_withNormalMedioPago_shouldAccumulateCorrectly() {
        // Configurar


        // Ejecutar
        listener.afterProcess(inputDto, outputDocument.toBuilder().medioPago("PAT") .build());

        // Verificar
        assertTrue(dataForHml.containsKey("PAT_TOTAL_AMOUNT"));
        assertTrue(dataForHml.containsKey("PAT_TOTAL_NUMBER"));

        assertEquals(1500.75, dataForHml.get("PAT_TOTAL_AMOUNT").doubleValue());
        assertEquals(1.0, dataForHml.get("PAT_TOTAL_NUMBER").doubleValue());
    }

    @Test
    void afterProcess_withBlankMedioPago_shouldUseNotDefinedKey() {

        // Ejecutar
        listener.afterProcess(inputDto, outputDocument.toBuilder().medioPago("") .build());

        // Verificar
        assertTrue(dataForHml.containsKey("NOT_DEFINED_TOTAL_AMOUNT"));
        assertTrue(dataForHml.containsKey("NOT_DEFINED_TOTAL_NUMBER"));

        assertEquals(1500.75, dataForHml.get("NOT_DEFINED_TOTAL_AMOUNT").doubleValue());
        assertEquals(1.0, dataForHml.get("NOT_DEFINED_TOTAL_NUMBER").doubleValue());
    }

    @Test
    void afterProcess_withSpacesInMedioPago_shouldReplaceWithUnderscores() {


        // Ejecutar
        listener.afterProcess(inputDto, outputDocument.toBuilder().medioPago("TARJETA DE CREDITO") .build());

        // Verificar
        assertTrue(dataForHml.containsKey("TARJETA_DE_CREDITO_TOTAL_AMOUNT"));
        assertTrue(dataForHml.containsKey("TARJETA_DE_CREDITO_TOTAL_NUMBER"));

        assertEquals(1500.75, dataForHml.get("TARJETA_DE_CREDITO_TOTAL_AMOUNT").doubleValue());
        assertEquals(1.0, dataForHml.get("TARJETA_DE_CREDITO_TOTAL_NUMBER").doubleValue());
    }

    @Test
    void afterProcess_multipleDocumentsOfSameType_shouldAccumulateValues() {
        // Primer documento
        listener.afterProcess(inputDto, outputDocument.toBuilder().medioPago("PAC") .build());

        // Segundo documento
        Document secondDocument = Document.builder()
                .medioPago("PAC")
                .totalAPagar(new BigDecimal("2500.25"))
                .build();
        listener.afterProcess(inputDto, secondDocument);

        // Verificar
        assertEquals(4001.0, dataForHml.get("PAC_TOTAL_AMOUNT").doubleValue(), 0.01);
        assertEquals(2.0, dataForHml.get("PAC_TOTAL_NUMBER").doubleValue());
    }


    @Test
    void afterProcess_withZeroAmount_shouldStillAccumulateCounter() {

        // Ejecutar
        listener.afterProcess(inputDto, outputDocument.toBuilder()
                        .medioPago("PAC")
                        .totalAPagar(BigDecimal.ZERO)
                .build());

        // Verificar
        assertEquals(0.0, dataForHml.get("PAC_TOTAL_AMOUNT").doubleValue());
        assertEquals(1.0, dataForHml.get("PAC_TOTAL_NUMBER").doubleValue());
    }
}