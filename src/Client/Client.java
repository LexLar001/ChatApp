package Client;

import Connection.Connection;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private Connection connection;
    private static ModelGuiClient model;
    private static ViewGuiClient gui;
    private boolean isConnect = false; //прапор стану підключення клієнта

    public boolean isConnect() {
        return isConnect;
    }
    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public static void main(String[] agrs) {

        Client client = new Client();
        model = new ModelGuiClient();
        gui = new ViewGuiClient(client);
        gui.initFrameClient();

        while (true) {
            if(client.isConnect()) {
                client.setConnect(false);
            }
        }
    }

}

