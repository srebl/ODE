package at.fhtechnikumwien.ode.common;

import at.fhtechnikumwien.ode.common.messages.Message;
import at.fhtechnikumwien.ode.common.messages.ResponseMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.*;

public class SocketHandler {
    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final ConcurrentMap<UUID, CompletableFuture<Result<Message<?>, String>>> callBacks = new ConcurrentHashMap<>();
    private final MessageHandler handler;

    public SocketHandler(Socket socket, MessageHandler handler) throws IOException {
        this.socket = socket;
        this.dis = new DataInputStream(socket.getInputStream());
        this.dos = new DataOutputStream(socket.getOutputStream());
        this.handler = handler;
        new Thread(this::recieveMsgAsync).start();
    }

    public ConcurrentMap<UUID, CompletableFuture<Result<Message<?>, String>>> getCallBacks(){
        return callBacks;
    }

    public Result<Message<?>, String> sendMsgWithAck(Message<?> msg){
        Enviroment.logg("start sendMsgWithAck");
        final CompletableFuture<Result<Message<?>, String>> ft = new CompletableFuture<>();
        Enviroment.logg("send: created future.");
        CompletableFuture<Result<Message<?>, String>> call = callBacks.putIfAbsent(msg.uuid, ft);
        Enviroment.logg("send: created callback");
        if(call != null){
            String str = "sendMsg: a callback for the message has already been registered.";
            Enviroment.logg(str);
            return Result.err(str);
        }

        Enviroment.logg("sending messsage");
        Result<Boolean, String> r_send = send(msg);
        if(r_send.isErr()){
            return Result.err(r_send.getErr());
        }

        //wait till server answers
        try {
            var blub = ft.get(/*2, TimeUnit.SECONDS*/);
            Enviroment.logg("send msg success");
            return blub;
        } catch (InterruptedException | ExecutionException /*| TimeoutException*/ e) {
            callBacks.remove(msg.uuid);
            Enviroment.logg(e.getMessage());
            return Result.err(e.getMessage());
        }
    }

    public Result<Boolean, String> sendMsg(Message<?> msg){
        return send(msg);
    }

    void recieveMsgAsync(){

        while (true){
            Enviroment.logg("start recieving");
            var res = receive();
            if(res.isErr()){
                Enviroment.instance().getLogger().logg(res.getErr());
            } else {
                handler.handleMessage(res.unwrap(), this);
            }
        }

    }

    private Result<Message<?>, String> receive(){
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
            Message<?> msg = (Message<?>) cls.getDeclaredConstructor().newInstance();
            //deserialize data into Message
            byte[] data = dis.readNBytes(msgLength - i);
            msg = msg.deserialize(data);
            String json = msg.getMapper().writeValueAsString(msg);
            System.out.println(json);
            System.out.println("finished reading msg");
            return Result.ok(msg);
        } catch (IOException | ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException e) {
            return Result.err(e.getMessage());
        }
    }

    private Result<Boolean, String> send(Message<?> msg){
        try
        {
            Enviroment.logg("send: start serialize");
            byte[] data = msg.serialize();
            String msg_str = new String(data);
            Enviroment.logg("data to send: %s" + msg_str);
            dos.write(data);
            dos.flush();
            Enviroment.logg("send data: %s" + msg_str);
            return Result.ok(true);
        } catch (Exception e) {
            Enviroment.logg(e.getMessage());
            return Result.err(e.getMessage());
        }
    }

    public void closeSocket(){
        close(dis);
        close(dos);
        close(socket);
    }

    private void close(AutoCloseable closeable){
        try {
            closeable.close();
        } catch (Exception e) {
            return;
        }
    }
}
