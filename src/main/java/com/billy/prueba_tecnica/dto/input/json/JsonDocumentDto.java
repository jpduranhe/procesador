package com.billy.prueba_tecnica.dto.input.json;

import lombok.Builder;

@Builder
public record JsonDocumentDto(
        String DocType,
        String NroDocInterno,
        String ContentBase64
) {}
