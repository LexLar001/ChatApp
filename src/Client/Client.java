package Client;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private boolean isConnect = false; //прапор стану підключення клієнта

    public boolean isConnect() {
        return isConnect;
    }
    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public static void main(String[] agrs) {
        Client client = new Client();

        while (true) {
            if(client.isConnect()) {
                client.setConnect(false);
            }
        }
    }

}

