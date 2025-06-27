package com.billy.prueba_tecnica.service.impl;

import com.billy.prueba_tecnica.configuration.ReportHtmlProperty;
import com.billy.prueba_tecnica.service.ReportHtmlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportHtmlGeneratorImpl implements ReportHtmlGenerator {

    private final ReportHtmlProperty reportHtmlProperty;
    private final ConcurrentMap<String, DoubleAccumulator> dataForHml;

    @Override
    public void generateReport()throws Exception {


        generateFileReport("final_report",generateHtmlString());
    }


    private void generateFileReport(String fileName, String content) throws Exception {
        Path directoryPath = Paths.get(reportHtmlProperty.getPath());
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
            log.info("Directorio de reportes creado en: {}", directoryPath.toAbsolutePath());
        }


        String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            fileName = fileName + "_" + timestamp + ".html";


        // Ruta completa del archivo
        Path filePath = directoryPath.resolve(fileName);

        try (FileWriter writer = new FileWriter(filePath.toFile(), StandardCharsets.UTF_8)) {
            writer.write(content);
            log.info("Reporte HTML generado correctamente: {}", filePath.toAbsolutePath());
        } catch (IOException e) {
            log.error("Error al generar el reporte HTML: {}", e.getMessage());
            throw new Exception("Error al generar el reporte HTML", e);
        }
    }
    private String generateHtmlString(){

        var baseHtml= """
                <html>
                    <body >
                        <h1>Reporte Procesamiento de Datos</h1>
                
                        <table border="1" style="width: 100%; border-collapse: collapse;">
                            <thead border="1" style="width: 100%; border-collapse: collapse;">
                                <tr>
                                    __th__
                                </tr>
                            </thead>
                            <tbody>
                                    __totalDocs__
                                    __totalAmount__                                    
                            </tbody>
                        </table>
                    </body>
                </html>
                """;
        StringBuilder thBuilder = new StringBuilder();
        StringBuilder totalDocsyBuilder = new StringBuilder();
        StringBuilder totalAmountBuilder = new StringBuilder();


        totalDocsyBuilder.append("<tr>").append("<td>Cantidad Docs</td>");
        totalAmountBuilder.append("<tr>").append("<td>Total a Pagar</td>");


        thBuilder.append("<th>").append("</th>");
        dataForHml.entrySet().stream()
                .map( data-> data.getKey().replace("_TOTAL_AMOUNT","").replace("_TOTAL_NUMBER", ""))
                .collect(Collectors.toSet())
                .forEach(tipos -> {


                    thBuilder.append("<th>").append(tipos).append("</th>");

                    totalDocsyBuilder
                            .append("<td>").append(dataForHml.get(tipos.concat("_TOTAL_NUMBER"))).append("</td>");
                    totalAmountBuilder
                            .append("<td>").append(dataForHml.get(tipos.concat("_TOTAL_AMOUNT"))).append("</td>");



                });
        thBuilder.append("<th>").append("TOTALES").append("</th>");

        var totalDocsfinal = dataForHml.entrySet().stream()
                .filter(entry -> entry.getKey().endsWith("_TOTAL_NUMBER"))
                .mapToDouble(entry -> entry.getValue().doubleValue())
                .sum();

        var totalAmountFinal = dataForHml.entrySet().stream()
                .filter(entry -> entry.getKey().endsWith("_TOTAL_AMOUNT"))
                .mapToDouble(entry -> entry.getValue().doubleValue())
                .sum();



        totalDocsyBuilder.append("<td>").append(totalDocsfinal).append("</td>").append("</tr>");
        totalAmountBuilder.append("<td>").append(totalAmountFinal).append("</td>").append("</tr>");


        return baseHtml
                .replace("__th__", thBuilder.toString())
                .replace("__totalDocs__", totalDocsyBuilder.toString())
                .replace("__totalAmount__", totalAmountBuilder.toString());
    }
}
