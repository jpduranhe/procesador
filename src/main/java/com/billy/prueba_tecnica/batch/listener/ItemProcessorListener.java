package com.billy.prueba_tecnica.batch.listener;

import com.billy.prueba_tecnica.domain.Document;
import com.billy.prueba_tecnica.dto.input.json.JsonDocumentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.DoubleAccumulator;

@RequiredArgsConstructor
@Component
@Slf4j
public class ItemProcessorListener implements ItemProcessListener<JsonDocumentDto, Document> {

    private final ConcurrentMap<String, DoubleAccumulator > dataForHml;


    /**
     * Este método se invoca antes de procesar cada elemento.
     * Aquí puedes realizar cualquier acción necesaria antes del procesamiento.
     *
     * @param item el elemento a procesar
     */
    @Override
    public void afterProcess(JsonDocumentDto item, Document result) {
        ItemProcessListener.super.afterProcess(item, result);
        var type=(result.getMedioPago().isBlank())?  "NOT_DEFINED" : result.getMedioPago().toUpperCase().replace(" ", "_");
        var typeTotalAmount= type.concat("_TOTAL_AMOUNT");
        var typeTotalNumber= type.concat("_TOTAL_NUMBER");


        // Obtenemos o creamos los acumuladores
        dataForHml.computeIfAbsent(typeTotalAmount, k -> new DoubleAccumulator(Double::sum, 0.0));
        dataForHml.computeIfAbsent(typeTotalNumber, k -> new DoubleAccumulator(Double::sum, 0.0));

        // Acumulamos los valores
        dataForHml.get(typeTotalAmount).accumulate(result.getTotalAPagar().doubleValue());
        dataForHml.get(typeTotalNumber).accumulate(1.0);

    }


}
