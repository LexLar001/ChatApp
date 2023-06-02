package Server;

import Connection.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;


public class Server {

    private ServerSocket serverSocket;
    private static ViewGuiServer gui;
    private static ModelGuiServer model;
    private static boolean isServerStart = false; //прапор для стану сервера

    //метод, який запускає сервер
    protected void startServer(int port) {
        try{
            //якщо сокет немає посилання або не запущений
            serverSocket = new ServerSocket(port);
            isServerStart = true;
            gui.refreshDialogWindowServer("The server is running.\n");
        } catch (Exception e) {
            gui.refreshDialogWindowServer("Failed to start server.\n");
        }
    }

    //метод, який зупиняє сервер
    protected void stopServer() {
        try {
            //якщо серверний сокет існує та не закритий
            if (serverSocket != null && !serverSocket.isClosed()) {
                for (Map.Entry<String, Connection> user : model.getAllUsersMultiChat().entrySet()) {
                    user.getValue().close();
                }

                serverSocket.close();
                model.getAllUsersMultiChat().clear();
                gui.refreshDialogWindowServer("The server has been stopped.\n");
            } else {
                gui.refreshDialogWindowServer("The server is not running - nothing to stop!\n");
            }
        } catch (Exception e) {
            gui.refreshDialogWindowServer("Failed to stop the server.\n");
        }
    }

    //метод, в якому сервер приймає нові сокетні підключення від клієнта
    protected void acceptServer() {
        //
    }

    //метод, який розсилає повідомлення всім користувачам з мапи
    protected void sendMessageAllUsers(Message message) {
        for(Map.Entry<String, Connection> user : model.getAllUsersMultiChat().entrySet()) {
            try {
                user.getValue().send(message);
            } catch (Exception e) {
                gui.refreshDialogWindowServer("Error sending message to all users!\n");
            }
        }
    }

    //точка входу для серверного додатку
    public static void main(String[] args) {
        Server server = new Server();
        gui = new ViewGuiServer(server);
        model = new ModelGuiServer();
        gui.initFrameServer();

        while(true) {
            if(isServerStart) {
                server.acceptServer();
                isServerStart = false;
            }
        }
    }

    //клас-потік, який запускається, коли сервер приймає сокет нового клієнта
    //











}
