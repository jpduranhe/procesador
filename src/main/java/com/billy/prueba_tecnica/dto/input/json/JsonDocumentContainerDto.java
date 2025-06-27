package com.billy.prueba_tecnica.dto.input.json;

import java.util.List;


public record JsonDocumentContainerDto(
        List<JsonDocumentDto> documents
) {}