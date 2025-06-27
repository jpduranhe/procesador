package com.billy.prueba_tecnica.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class DecodificadorBase64GzipUtilTest {

    DecodificadorBase64GzipUtil decodificadorBase64GzipUtil ;

    @BeforeEach
    void setUp() {
        decodificadorBase64GzipUtil = new DecodificadorBase64GzipUtil();
    }

    @Test
    void testDecodeReturnsCorrectString() throws  JSONException {
        String base64GzipContent = "H4sIACU9XGgC/6vmUlBQykxRslJQMjYAAUMDJR2QWF5ibipI1DknMzWvJFUBJl5SWQAWd3N0DgkNcoQK5pck5jgGJKYnFgHlDM0sDfQMzMEyuakpmflAiXyQngDHECWuWgC6dcPDcwAAAA==";
        JSONObject result = decodificadorBase64GzipUtil.decode(base64GzipContent);
        var expected = new JSONObject("""
            {
              "id": "30000010",
              "name": "Cliente 10",
              "type": "FACTURA",
              "totalAPagar": 1690.07,
              "medioPago": "PAT"
            }""") ;
        assertNotNull(result);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    void testDecodeReturnsEmptyWhenInputIsNull() {
        JSONObject result = decodificadorBase64GzipUtil.decode(null);
        assertNull(result);
    }

    @Test
    void testDecodeReturnsEmptyWhenInputIsEmpty()  {
        JSONObject result = decodificadorBase64GzipUtil.decode("");
        assertNull( result);
    }

    @Test
    void testDecodeThrowsExceptionWhenInputIsNotBase64() {
        String invalidContent = "NoEsBase64";
        JSONObject result = decodificadorBase64GzipUtil.decode(invalidContent);
        assertNull( result);
    }

    @Test
    void testDecodeThrowsIOExceptionWhenInputIsNotGzipped() {
        String nonGzipBase64Content = Base64.getEncoder().encodeToString("PlainText".getBytes());
        JSONObject result = decodificadorBase64GzipUtil.decode(nonGzipBase64Content);
        assertNull( result);

    }

}