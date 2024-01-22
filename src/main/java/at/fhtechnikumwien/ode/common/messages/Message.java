package at.fhtechnikumwien.ode.common.messages;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(LogInMessage.class),
        @JsonSubTypes.Type(ResponseMessage.class),
})
public abstract class Message<T extends Message<T>> {
    @JsonIgnore
    final String className = getClass().getCanonicalName();
    @JsonIgnore
    final byte[] nameBytes = className.getBytes();
    @JsonIgnore
    public static final char delimiter = '#';

    public UUID uuid = UUID.randomUUID();

    @JsonIgnore
    public abstract byte[] getPayload();

    @JsonIgnore
    public byte[] serialize(){
        byte[] payload = getPayload();
        int msgSize =
                //Integer.BYTES //size of size Field
                Character.BYTES //size of all delimiters
                + nameBytes.length //size of msg type
                + payload.length; //size of payload
        ByteBuffer buff = ByteBuffer.allocate(Integer.BYTES + msgSize);
        buff.putInt(msgSize);
        buff.put(nameBytes);
        buff.putChar(delimiter);
        buff.put(payload);

        System.out.println(className);
        System.out.println(msgSize);
        System.out.println(new String(nameBytes, StandardCharsets.UTF_8));

        String blub = new String(payload);
        System.out.println(blub);

        return buff.array();
    }

    @JsonIgnore
    public abstract T deserialize(byte[] msg);

    @JsonIgnore
    public ObjectMapper getMapper(){
        //https://github.com/FasterXML/jackson-docs/wiki/JacksonPolymorphicDeserialization
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTyping(ptv); // default to using DefaultTyping.OBJECT_AND_NON_CONCRETE
        mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
        return mapper;
    }
}
