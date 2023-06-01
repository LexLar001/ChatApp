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

    protected void connectToServer() {
        //Якщо клієнт ще не підключений
        if(!isConnect) {
            while(true) {
                try {
                    //
                    String addressServer = gui.getServerAddress();
                    int port = gui.getPortServer();

                    Socket socket = new Socket(addressServer, port);
                    connection = new Connection(socket);
                    isConnect = true;
                    gui.addMessage("You are connected to the Server!\n");
                    break;
                } catch (Exception e) {
                    gui.errorDialogWindow("An error has occurred! You may have entered the wrong server address and port. Try again");
                    break;
                }
            }
        } else {
            gui.errorDialogWindow("You are already connected");
        }
    }

}

