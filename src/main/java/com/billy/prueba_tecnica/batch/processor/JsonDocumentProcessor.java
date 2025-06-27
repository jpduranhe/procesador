package com.billy.prueba_tecnica.batch.processor;

import com.billy.prueba_tecnica.domain.Document;
import com.billy.prueba_tecnica.dto.input.json.JsonDocumentDto;
import com.billy.prueba_tecnica.mapper.DocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JsonDocumentProcessor implements ItemProcessor<JsonDocumentDto, Document> {

    private final DocumentMapper documentMapper;
    @Override
    public Document process(JsonDocumentDto item) throws Exception {
         return documentMapper.fromJsonStringBase64Gzip(item.ContentBase64());
    }
}
