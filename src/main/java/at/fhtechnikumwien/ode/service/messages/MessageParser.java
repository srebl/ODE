package at.fhtechnikumwien.ode.service.messages;

import at.fhtechnikumwien.ode.common.messages.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MessageParser {
    public static Message<?> deserialize(DataInputStream dis, DataOutputStream dos){
        Message<?> msg = null;
        try
        {
            System.out.println("get message length");
            int msgLength = dis.readInt();
            System.out.printf("message length is %d\n", msgLength);

            int i = 0;
            char c;
            StringBuilder builder = new StringBuilder();
            do{
                c = dis.readChar();
                builder.append(c);
                i += Character.BYTES;
                //current = ((int)(data[i++])) << Byte.SIZE + data[i++];
                //builder.append((char)current);
            } while (c != Message.delimiter);

            //remove trailing delimiter'#'
            builder.deleteCharAt(builder.length()-1);
            System.out.printf("class name is %s\n", builder);
            Class<?> cls = Class.forName(builder.toString());
            //get class by reflection
            msg = (Message<?>) cls.getDeclaredConstructor().newInstance();
            //deserialize data into Message
            byte[] data = dis.readNBytes(msgLength - i);
            msg = msg.deserialize(data);
            System.out.println("deserialized msg");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return msg;
    }
}
