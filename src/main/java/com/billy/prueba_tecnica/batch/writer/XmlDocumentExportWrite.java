package com.billy.prueba_tecnica.batch.writer;

import com.billy.prueba_tecnica.domain.Document;
import com.billy.prueba_tecnica.dto.output.xml.XmlDTEDto;
import com.billy.prueba_tecnica.mapper.DocumentMapper;
import com.billy.prueba_tecnica.utils.GenerateXmlUtil;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
@RequiredArgsConstructor
public class XmlDocumentExportWrite {

    private final GenerateXmlUtil generateXmlUtil;
    private final DocumentMapper documentMapper;

    public void write(Chunk<? extends Document> chunkDocuments) {



        chunkDocuments.getItems()
                .forEach(document -> {
                    try {
                        var xmlDocumentDto = documentMapper.toXmlDocumentDto(document);
                        if (xmlDocumentDto == null) return;
                        generateXmlUtil.writeXml(new XmlDTEDto("1.0", xmlDocumentDto));
                    } catch (JAXBException | IOException  e) {
                        log.error("El documento {} fallo al escribir el xml: {}",document.getId(),e.getMessage(), e);
                    }
                });



    }

}
