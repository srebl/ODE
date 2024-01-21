package at.fhtechnikumwien.ode.common.messages;

import at.fhtechnikumwien.ode.common.ResponseType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseMessage extends Message<ResponseMessage>{
    public ResponseType type;
    public String message;
    @Override
    public byte[] getPayload() {
        ObjectMapper mapper = null;
        return new byte[0];
    }

    @Override
    public ResponseMessage deserialize(byte[] msg) {
        return null;
    }
}
