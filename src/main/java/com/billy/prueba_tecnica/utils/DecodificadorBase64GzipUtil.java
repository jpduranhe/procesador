package com.billy.prueba_tecnica.utils;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

@Component
public class DecodificadorBase64GzipUtil {


    /**
     * Decodifica un string en formato Base64 y luego descomprime el contenido GZIP
     *
     * @param textBase64Gzip String en formato Base64 que contiene datos comprimidos con GZIP
     * @return String decodificado y descomprimido
     * @throws IOException si ocurre un error durante la decodificación o descompresión
     * @throws IllegalArgumentException si ocurre el texto no esta en base64
     */
    public JSONObject decode(String textBase64Gzip){
        try{
            if (textBase64Gzip == null || textBase64Gzip.isEmpty()) {
                return null;
            }
            isBase64Valid(textBase64Gzip);

            byte[] bytesDecodificados = Base64.getDecoder().decode(textBase64Gzip);


            ByteArrayInputStream bis = new ByteArrayInputStream(bytesDecodificados);
            GZIPInputStream gis = new GZIPInputStream(bis);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }


            gis.close();
            bis.close();
            bos.close();

            var decodedString= bos.toString(StandardCharsets.UTF_8);
            return new JSONObject(decodedString);
        }
        catch (Exception e){
            return null;
        }

    }

    private static void isBase64Valid(String contenidoBase64) throws IllegalArgumentException {
        if (contenidoBase64 == null || contenidoBase64.isEmpty()) {
            throw new IllegalArgumentException("El contenido no puede ser nulo o vacío");
        }

        // Verificar que la longitud es múltiplo de 4 (con posible padding)
        if (contenidoBase64.length() % 4 != 0) {
            throw new IllegalArgumentException("La longitud del contenido debe ser un múltiplo de 4");
        }

        // Verificar que solo contiene caracteres válidos de Base64
        if (!contenidoBase64.matches("^[A-Za-z0-9+/]*={0,2}$")) {
            throw new IllegalArgumentException("El contenido contiene caracteres no válidos para Base64");
        }

        // Intentar decodificar
        try {
            Base64.getDecoder().decode(contenidoBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El contenido no es una cadena Base64 válida", e);
        }
    }

}
