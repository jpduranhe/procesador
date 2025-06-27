package com.billy.prueba_tecnica.dto.output.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;



@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlDocumentDto implements Serializable {
    private static final long serialVersionUID = -7218270070078074568L;

        @XmlAttribute(name = "ID")
        private  String id;

        @XmlElement(name = "Cliente")
        private    String cliente;

        @XmlElement(name = "Tipo")
        private    String tipo;

        @XmlElement(name = "TotalAPagar")
        private    BigDecimal totalAPagar;

        @XmlElement(name = "MedioPago")
        private     String medioPago;
}
