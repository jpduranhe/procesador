package com.billy.prueba_tecnica.batch.reader;


import com.billy.prueba_tecnica.dto.input.json.JsonDocumentDto;
import com.billy.prueba_tecnica.dto.input.json.JsonDocumentContainerDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.json.JsonObjectReader;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DocumentContainerJsonReader implements JsonObjectReader<JsonDocumentDto> {

    private JsonParser jsonParser;
    private ObjectMapper mapper;
    private InputStream inputStream;
    private List<JsonDocumentDto> jsonDocumentDtos;
    private int currentIndex = 0;

    public DocumentContainerJsonReader() {
        this(new ObjectMapper());
    }

    public DocumentContainerJsonReader(ObjectMapper mapper) {
        Assert.notNull(mapper, "El mapper no puede ser nulo");
        this.mapper = mapper;
        this.jsonDocumentDtos = new ArrayList<>();
    }

    public void setMapper(ObjectMapper mapper) {
        Assert.notNull(mapper, "El mapper no puede ser nulo");
        this.mapper = mapper;
    }

    @Override
    public void open(Resource resource) throws Exception {
        Assert.notNull(resource, "El recurso no puede ser nulo");
        this.inputStream = resource.getInputStream();
        this.jsonParser = this.mapper.getFactory().createParser(this.inputStream);

        // Leer el contenedor de documentos
        JsonDocumentContainerDto container = this.mapper.readValue(this.jsonParser, JsonDocumentContainerDto.class);
        if (container != null && container.documents() != null) {
            this.jsonDocumentDtos = container.documents();
        }

        this.currentIndex = 0;
    }

    @Nullable
    @Override
    public JsonDocumentDto read() throws Exception {
        try {
            if (currentIndex < jsonDocumentDtos.size()) {
                return jsonDocumentDtos.get(currentIndex++);
            }
            return null;
        } catch (Exception e) {
            throw new ParseException("No se pudo leer el siguiente documento", e);
        }
    }

    @Override
    public void close() throws Exception {
        if (this.inputStream != null) {
            this.inputStream.close();
        }
        if (this.jsonParser != null) {
            this.jsonParser.close();
        }
    }

    @Override
    public void jumpToItem(int itemIndex) throws Exception {
        if (itemIndex >= 0 && itemIndex < jsonDocumentDtos.size()) {
            this.currentIndex = itemIndex;
        }
    }
}