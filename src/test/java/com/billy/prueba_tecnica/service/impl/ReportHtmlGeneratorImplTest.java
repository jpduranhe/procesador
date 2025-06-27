package com.billy.prueba_tecnica.service.impl;

import com.billy.prueba_tecnica.configuration.ReportHtmlProperty;
import com.billy.prueba_tecnica.service.ReportHtmlGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.DoubleAccumulator;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ReportHtmlGeneratorImplTest {

    private ReportHtmlGenerator reportHtmlGenerator;

    @Mock
    private ReportHtmlProperty reportHtmlProperty;
    @TempDir
    Path tempDir;


    private ConcurrentMap<String, DoubleAccumulator> dataForHml;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(reportHtmlProperty.getPath()).thenReturn(tempDir.toString());
        dataForHml = new ConcurrentHashMap<>();

        dataForHml.put("NOT_DEFINED_TOTAL_AMOUNT", new DoubleAccumulator(Double::sum, 1717068.1));
        dataForHml.put("PAT_TOTAL_AMOUNT", new DoubleAccumulator(Double::sum, 1681348.68));
        dataForHml.put("PAC_TOTAL_AMOUNT", new DoubleAccumulator(Double::sum, 1532908.44));
        dataForHml.put("PAC_TOTAL_NUMBER", new DoubleAccumulator(Double::sum, 309));
        dataForHml.put("NOT_DEFINED_TOTAL_NUMBER", new DoubleAccumulator(Double::sum, 345));
        dataForHml.put("PAT_TOTAL_NUMBER", new DoubleAccumulator(Double::sum, 346));


        reportHtmlGenerator= new ReportHtmlGeneratorImpl(reportHtmlProperty,dataForHml);
    }


    @Test
    void generateReport_shouldCreateHtmlFile() throws Exception {
        // Ejecutar
        reportHtmlGenerator.generateReport();

        // Verificar
        Path[] files = Files.list(tempDir).toArray(Path[]::new);
        assertEquals(1, files.length, "Debe generarse exactamente un archivo");

        String fileName = files[0].getFileName().toString();
        assertTrue(fileName.startsWith("final_report_"), "El nombre del archivo debe comenzar con 'final_report_'");
        assertTrue(fileName.endsWith(".html"), "El archivo debe tener extensión .html");

        String content = Files.readString(files[0]);
        assertTrue(content.contains("<h1>Reporte Procesamiento de Datos</h1>"), "El contenido debe tener el título");
        assertTrue(content.contains("<td>1000.0</td>"), "El contenido debe mostrar el total de documentos");
        assertTrue(content.contains("<td>4931325.22</td>"), "El contenido debe mostrar el monto total");
    }

    @Test
    void generateReport_shouldContainAllDataTypes() throws Exception {
        // Ejecutar
        reportHtmlGenerator.generateReport();

        // Verificar
        Path[] files = Files.list(tempDir).toArray(Path[]::new);
        String content = Files.readString(files[0]);

        // Verificar que todos los tipos aparecen en el reporte
        assertTrue(content.contains("<th>NOT_DEFINED</th>"), "El reporte debe contener el tipo NOT_DEFINED");
        assertTrue(content.contains("<th>PAT</th>"), "El reporte debe contener el tipo PAT");
        assertTrue(content.contains("<th>PAC</th>"), "El reporte debe contener el tipo PAC");
    }

    @Test
    void generateReport_shouldCalculateTotalsCorrectly() throws Exception {
        // Ejecutar
        reportHtmlGenerator.generateReport();

        // Verificar
        Path[] files = Files.list(tempDir).toArray(Path[]::new);
        String content = Files.readString(files[0]);

        // Verificar que se calculan correctamente los totales
        assertTrue(content.contains("<td>1000.0</td>"), "Total de documentos debe ser 1000");
        assertTrue(content.contains("<td>4931325.22</td>"), "Total de montos debe ser 4931325.22");
    }

    @Test
    void generateReport_withNoData_shouldCreateEmptyReport() throws Exception {
        // Preparar
        dataForHml.clear();

        // Ejecutar
        reportHtmlGenerator.generateReport();

        // Verificar
        Path[] files = Files.list(tempDir).toArray(Path[]::new);
        assertEquals(1, files.length, "Debe generarse un archivo incluso sin datos");

        String content = Files.readString(files[0]);
        assertTrue(content.contains("<h1>Reporte Procesamiento de Datos</h1>"), "El contenido debe tener el título");
        assertTrue(content.contains("<td>0.0</td>"), "El total debe ser 0.0 cuando no hay datos");
    }

    @Test
    void generateReport_whenDirectoryDoesNotExist_shouldCreateDirectory() throws Exception {
        // Preparar
        Path nonExistentDir = tempDir.resolve("nonexistent");
        when(reportHtmlProperty.getPath()).thenReturn(nonExistentDir.toString());

        // Ejecutar
        reportHtmlGenerator.generateReport();

        // Verificar
        assertTrue(Files.exists(nonExistentDir), "El directorio debe ser creado si no existe");
        assertEquals(1, Files.list(nonExistentDir).count(), "Debe contener un archivo");
    }

}