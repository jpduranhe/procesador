package com.billy.prueba_tecnica.domain;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@Value
public class Document {
    String id;
    String name;
    String type;
    BigDecimal totalAPagar;
    String medioPago;
}
