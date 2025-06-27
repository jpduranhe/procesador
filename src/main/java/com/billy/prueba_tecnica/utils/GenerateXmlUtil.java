package com.billy.prueba_tecnica.utils;

import com.billy.prueba_tecnica.configuration.FileResultProperty;
import com.billy.prueba_tecnica.dto.output.xml.XmlDTEDto;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateXmlUtil {

    private  final FileResultProperty fileResultProperty;

    private String generateXML(XmlDTEDto dte)  {
        try{
            StringWriter sw = new StringWriter();

            JAXBContext context = JAXBContext.newInstance(XmlDTEDto.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "ISO-8859-1");
            sw.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            marshaller.marshal(dte, sw);
            sw.write("\n</xml>");
            return sw.toString();
        }catch (Exception e){
            log.error("Error al generar el documento XML: {}", e.getMessage(),e);
            return  null;
        }
    }
    public void writeXml(XmlDTEDto dte) throws JAXBException, IOException {
        String xmlContent = generateXML(dte);
        if(xmlContent == null){return;}
        Path basePath = Paths.get(fileResultProperty.getPath());
        String fileName = getNameFile(dte);
        Path fullPath = basePath.resolve(fileName);
        Files.createDirectories(fullPath.getParent());
        Files.writeString(fullPath, xmlContent, StandardCharsets.ISO_8859_1);
    }
    private String getNameFile(XmlDTEDto dte) {
        return  dte.getDocumento().getId().concat("_").concat(dte.getDocumento().getCliente().toLowerCase().replace(" ","_")).concat(".xml");
    }
}
