package it.librone.okipo.task.DTO;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.librone.okipo.task.Exceptions.GenericEthScanException;

import java.io.IOException;
import java.util.List;
/**
 * Custom deserializer per deserialize la risposta di EtherScan che può essere sia una stringa che un array
 */
public class CustomListDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();

        // Controllo se sia una stringa o un array
        if (jsonParser.currentToken() == JsonToken.VALUE_STRING) {
            return jsonParser.getText();  // Nel caso sia una stringa
        } else if (jsonParser.currentToken() == JsonToken.START_ARRAY) {
            return mapper.readValue(jsonParser, new TypeReference<List<Result>>() {});
        }

        throw new GenericEthScanException("Errore durante la deserializzazione della risposta di EtherScan");
    }

}
