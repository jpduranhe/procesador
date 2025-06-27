package com.billy.prueba_tecnica.utils;

import com.billy.prueba_tecnica.configuration.FileResultProperty;
import com.billy.prueba_tecnica.dto.output.xml.XmlDTEDto;
import com.billy.prueba_tecnica.dto.output.xml.XmlDocumentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateXmlUtilTest {

    @Mock
    private FileResultProperty fileResultProperty;


    private GenerateXmlUtil generateXmlUtil;

    @TempDir
    Path tempDir;

    private XmlDTEDto mockDte;

    @BeforeEach
    void setUp() {
        when(fileResultProperty.getPath()).thenReturn(tempDir.toString());
        // Configurar el mock de XmlDTEDto
        generateXmlUtil = new GenerateXmlUtil(fileResultProperty);
        XmlDocumentDto mockDocumento = new XmlDocumentDto();
        mockDocumento.setId("123");
        mockDocumento.setCliente("Cliente Prueba");
        mockDocumento.setTipo("Tipo Prueba");
        mockDocumento.setMedioPago("Tarjeta");
        mockDocumento.setTotalAPagar(BigDecimal.valueOf(3000));
        mockDte = new XmlDTEDto("1.0", mockDocumento);
    }

    @Test
    void testGenerateXML() throws Exception {
        // Esta prueba evalúa el método privado indirectamente a través de writeXml
        // Configurar el directorio temporal para la salida
        String tempPath = tempDir.toString() + "/";
        when(fileResultProperty.getPath()).thenReturn(tempPath);

        // Ejecutar el método
        generateXmlUtil.writeXml(mockDte);

        // Verificar que se haya creado un archivo
        Path expectedFilePath = Paths.get(tempPath + "123_cliente_prueba.xml");
        assertTrue(Files.exists(expectedFilePath));

        // Verificar contenido del archivo
        String content = Files.readString(expectedFilePath, StandardCharsets.ISO_8859_1);
        assertNotNull(content);
        assertTrue(content.contains("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>"));
        assertTrue(content.contains("</xml>"));
    }

    @Test
    void testWriteXml_DirectoryCreation() throws Exception {
        // Configurar un subdirectorio que no existe
        String nestedPath = tempDir.toString() + "/nested/dir/";
        when(fileResultProperty.getPath()).thenReturn(nestedPath);

        // Ejecutar el método
        generateXmlUtil.writeXml(mockDte);

        // Verificar que se haya creado el directorio
        Path dirPath = Paths.get(nestedPath);
        assertTrue(Files.exists(dirPath));

        // Verificar que se haya creado el archivo
        Path filePath = Paths.get(nestedPath + "123_cliente_prueba.xml");
        assertTrue(Files.exists(filePath));
    }


}