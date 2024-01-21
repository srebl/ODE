package at.fhtechnikumwien.ode.common.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

public class LogInMessage extends Message<LogInMessage>{
    public String number;

    //public LogInMessage(){};

    public LogInMessage(String number){
        this.number = number;
    }

    @Override
    public byte[] getPayload() {
        ObjectMapper mapper = new ObjectMapper();
        String str_val = "";
        try {
             str_val = mapper.writeValueAsString(this);
            byte[] blub = mapper.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return number.getBytes();
    }

    @Override
    public LogInMessage deserialize(byte[] msg) {
        this.number = new String(msg, StandardCharsets.UTF_8);
        return this;
    }
}
