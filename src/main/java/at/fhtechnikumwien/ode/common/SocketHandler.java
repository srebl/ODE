package at.fhtechnikumwien.ode.common;

import at.fhtechnikumwien.ode.common.messages.Message;
import at.fhtechnikumwien.ode.common.messages.ResponseMessage;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.*;

public class SocketHandler {
    private final ConcurrentMap<UUID, CompletableFuture<Result<Message<?>, String>>> callBacks = new ConcurrentHashMap<>();

    public Result<Boolean, String> initConnection(){
        Socket socket = Enviroment.instance().getSocket();
        String serverAddr = Enviroment.instance().getServerAddress();
        int port = Enviroment.instance().getServerPort();

        try{
            if(socket == null){
                socket = new Socket(serverAddr, port);
                Enviroment.instance().setSocket(socket);
            }
            return Result.ok(true);
        } catch (IOException e) {
            Enviroment.instance().getLogger().logg(e.getMessage());
            return Result.err(e.getMessage());
        }
    }

    public Result<Message<?>, String> sendMsg(Message<?> msg){
        final CompletableFuture<Result<Message<?>, String>> ft = new CompletableFuture<>();
        CompletableFuture<Result<Message<?>, String>> call = callBacks.putIfAbsent(msg.uuid, ft);
        if(call != null){
            return Result.err("sendMsg: a callback for the message has already been registered.");
        }

        Result<Boolean, String> r_send = SocketHandler.send(msg);
        if(r_send.isErr()){
            return Result.err(r_send.getErr());
        }

        //wait till server answers
        try {
            return ft.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            callBacks.remove(msg.uuid);
            return Result.err(e.getMessage());
        }
    }

    void recieveMsgAsync(){
        while (true){
            try {
                if(Enviroment.instance().getDis() == null) {
                    wait(1000);
                    continue;
                }
                var res = SocketHandler.receive();
                if(res.isErr()){
                    Enviroment.instance().getLogger().logg(res.getErr());
                } else {
                    handleRescMsg(res.unwrap());
                }
            } catch (InterruptedException e) {
                Enviroment.instance().getLogger().logg(e.getMessage());
            }
        }
    }

    private void handleRescMsg(Message<?> msg){
        if(msg instanceof ResponseMessage response) {
            CompletableFuture<Result<Message<?>, String>> callBack =
                    callBacks.remove(response.responeOfUuid);
            if (callBack == null) {
                //ignore msg
                return;
            }
            callBack.complete(Result.ok(msg));
        }
    }

    private static synchronized Result<Message<?>, String> receive(){
        DataInputStream dis = Enviroment.instance().getDis();
        if(dis == null){
            return Result.err("no input stream");
        }
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

    private static synchronized Result<Boolean, String> send(Message<?> msg){
        var dos = Enviroment.instance().getDos();
        if(dos == null){
            return Result.err("no output stream.");
        }
        try
        {
            byte[] data = msg.serialize();
            dos.write(data);
            dos.flush();
            String msg_str = new String(data);
            System.out.printf("send data: %s", msg_str);
            return Result.ok(true);
        } catch (Exception e) {
            return Result.err(e.getMessage());
        }
    }
}
