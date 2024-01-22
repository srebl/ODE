package at.fhtechnikumwien.ode.common.messages;

import at.fhtechnikumwien.ode.common.ResponseType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

public class ResponseMessage extends Message<ResponseMessage>{
    public ResponseType type;
    public String message;

    public UUID responeOfUuid;
    @Override
    public byte[] getPayload() {
        ObjectMapper mapper = getMapper();
        try {
            return mapper.writeValueAsBytes(this);
        } catch (JsonProcessingException e){
            e.printStackTrace();
            return new byte[0];
        }
    }

    @Override
    public ResponseMessage deserialize(byte[] msg) {
        ObjectMapper mapper = getMapper();
        try {
            return mapper.readValue(msg, ResponseMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
