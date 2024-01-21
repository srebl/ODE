package at.fhtechnikumwien.ode.common.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LogInMessage extends Message<LogInMessage>{
    public String number;

    public LogInMessage(){};

    public LogInMessage(String number){
        this.number = number;
    }

    @Override
    public byte[] getPayload() {
        try {
            ObjectMapper mapper = getMapper();
            return mapper.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LogInMessage deserialize(byte[] msg) {
        ObjectMapper mapper = getMapper();
        try {
            return mapper.readValue(msg, LogInMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
