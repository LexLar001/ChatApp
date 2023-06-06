package Connection;

import java.io.*;
import java.net.Socket;

public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }
    //Відправка по сокету повідомлення
    public void send(Message message) throws IOException {
        synchronized (this.out){
            out.writeObject(message);
        }
    }
    //Прийняття по сокету повідомлення
    public Message receive() throws IOException, ClassNotFoundException {
        synchronized (this.in){
            Message message = (Message) in.readObject();
            return message;
        }
    }
    //Для закріття потоків читання, запису та сокету
    @Override
    public void close() throws IOException{
        in.close();
        out.close();
        socket.close();
    }
}
