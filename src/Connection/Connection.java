package Connection;

import java.io.*;
import java.net.Socket;

public class Connection implements Closeable{
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    //Для закріття потоків читання, запису та сокету
    @Override
    public void close() throws IOException{
        in.close();
        out.close();
        socket.close();
    }
}
