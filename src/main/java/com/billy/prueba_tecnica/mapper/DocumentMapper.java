package com.billy.prueba_tecnica.mapper;

import com.billy.prueba_tecnica.domain.Document;
import com.billy.prueba_tecnica.dto.output.xml.XmlDocumentDto;
import com.billy.prueba_tecnica.utils.DecodificadorBase64GzipUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DocumentMapper {
    private final DecodificadorBase64GzipUtil decodificadorBase64GzipUtil;
    public Document fromJsonStringBase64Gzip(String textBase64Gzip) {
        if (textBase64Gzip == null) return null;
        try{
            JSONObject json = decodificadorBase64GzipUtil.decode(textBase64Gzip);
            return Document.builder()
                    .id(json.getString("id"))
                    .name(json.getString("name"))
                    .type(json.getString("type"))
                    .totalAPagar(BigDecimal.valueOf(json.getDouble("totalAPagar")))
                    .medioPago(json.getString("medioPago"))
                    .build();

        }
        catch (Exception e) {
            return null;
        }

    }
    public XmlDocumentDto toXmlDocumentDto(Document document) {
        if(document == null) return null;
        var xml= new XmlDocumentDto();
        xml.setId(document.getId());
        xml.setCliente(document.getName());
        xml.setTipo(document.getType());
        xml.setTotalAPagar(document.getTotalAPagar());
        xml.setMedioPago(document.getMedioPago());
        return xml;
    }


}
