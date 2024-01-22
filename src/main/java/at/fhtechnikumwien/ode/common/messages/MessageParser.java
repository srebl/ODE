package at.fhtechnikumwien.ode.common.messages;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MessageParser {
    public static Message<?> receive(DataInputStream dis){
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
                byte[] data = dis.readNBytes(1);
                c = (char) data[0];
                builder.append(c);
                System.out.printf("%c", c);
                i++;
            } while (c != Message.delimiter);
            System.out.println("deserialize data");
            //remove trailing delimiter'#'
            String className = builder.substring(0, builder.length()-2);
            System.out.printf("class name is %s\n", className);
            Class<?> cls = Class.forName(className);
            //get class by reflection
            msg = (Message<?>) cls.getDeclaredConstructor().newInstance();
            //deserialize data into Message
            byte[] data = dis.readNBytes(msgLength - i);
            msg = msg.deserialize(data);
            String json = msg.getMapper().writeValueAsString(msg);
            System.out.println(json);
            System.out.println("finished reading msg");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return msg;
    }

    public static void send(DataOutputStream dos, Message<?> msg){
        try
        {
            byte[] data = msg.serialize();
            dos.write(data);
            dos.flush();
            String msg_str = new String(data);
            System.out.printf("send data: %s", msg_str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
