package com.billy.prueba_tecnica.dto.output.xml;


import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "DTE")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlDTEDto implements Serializable {
    private static final long serialVersionUID = -2965402531347384187L;

    @XmlAttribute(name = "version")
    private String version;
    @XmlElement(name = "Documento")
    private XmlDocumentDto documento;

}
