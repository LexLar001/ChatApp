package Client;

import Connection.*;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private Connection connection;
    private static ModelGuiClient model;
    private static ViewGuiClient gui;
    private volatile boolean isConnect = false; //прапор стану підключення клієнта

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
                client.nameUserRegistration();
                client.receiveMessageFromServer();
                client.setConnect(false);
            }
        }
    }

    //метод підключення до серверу
    protected void connectToServer() {
        //Якщо клієнт ще не підключений
        if(!isConnect) {
            while(true) {
                try {
                    //заповнюємо адресу, порт та сокет
                    String addressServer = gui.getServerAddress();
                    int port = gui.getPortServer();
                    Socket socket = new Socket(addressServer, port);
                    connection = new Connection(socket);
                    isConnect = true;
                    gui.addMessage("Message: you are connected to the Server!\n");
                    break;
                } catch (Exception e) {
                    gui.errorDialogWindow("An error has occurred! You may have entered the wrong server address and port. Try again");
                    break;
                }
            }
        } else {
            gui.errorDialogWindow("You are already connected!");
        }
    }

    protected void nameUserRegistration() {
        while (true) {
            try {
                Message message = connection.receive();
                //повідомлення від сервера, якщо це запит та ім'я, то виводимо вікно вводу
                if(message.getTypeMessage() == MessageType.REQUEST_NAME_USER) {
                    String nameUser = gui.getNameUser();
                    connection.send(new Message(MessageType.USER_NAME, nameUser));
                }
                //якщо ім'я використовується, то виводимо вікно з повідомленням та перезаписуємо ім'я
                if (message.getTypeMessage() == MessageType.NAME_USED) {
                    gui.errorDialogWindow("This name is already in use, please enter another one");
                    String nameUser = gui.getNameUser();
                    connection.send(new Message(MessageType.USER_NAME, nameUser));
                }
                //якщо ім'я прийнято, отримуємо список користувачів та виводимо його
                if (message.getTypeMessage() == MessageType.NAME_ACCEPTED){
                    gui.addMessage("Message: your name is taken!");
                    model.setUsers(message.getListUsers());
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                gui.errorDialogWindow("An error occurred while registering the name. Try to reconnect");
                try {
                    connection.close();
                    isConnect = false;
                    break;
                } catch (IOException ex) {
                    gui.errorDialogWindow("Error closing connection");
                }
            }
        }
    }

    //метод відправки повідомлення іншим користувачам на сервер
    protected void sendMessageOnServer(String text){
        try {
            connection.send(new Message(MessageType.TEXT_MESSAGE, text));
        } catch (Exception e) {
            gui.errorDialogWindow("Error sending message");
        }
    }

    //метод, який приймає повідомлення інших з сервера
    protected void receiveMessageFromServer() {
        while (isConnect) {
            try{
                Message message = connection.receive();

                //якщо тип TEXT_MESSAGE, то додаємо повідомлення у вікно
                if (message.getTypeMessage() == MessageType.TEXT_MESSAGE) {
                    gui.addMessage(message.getTextMessage());
                }

                //якщо тип USER_ADDED, додаємо інфу про нового користувача
                if (message.getTypeMessage() == MessageType.USER_ADDED) {
                    model.addUser(message.getTextMessage());
                    gui.refreshListUsers(model.getUsers());
                    gui.addMessage(String.format("Message: user has %s joined the chat.\n", message.getTextMessage()));
                }
                //якщо тип USER_REMOVED, видаляємо користувача зі списку
                if (message.getTypeMessage() == MessageType.USER_REMOVED) {
                    model.removeUser(message.getTextMessage());
                    gui.refreshListUsers(model.getUsers());
                    gui.addMessage(String.format("Message: user %s has left the chat.\n", message.getTextMessage()));
                }
            } catch (Exception e) {
                gui.errorDialogWindow("Error while receiving message from server");
                setConnect(false);
                gui.refreshListUsers(model.getUsers());
                break;
            }
        }
    }

    //метод, який реалізує відключення клієнта від чату
    protected void disableClient() {
        try {
            if(isConnect) {
                connection.send(new Message(MessageType.DISABLE_USER));
                model.getUsers().clear();
                isConnect = false;
                gui.refreshListUsers(model.getUsers());
            } else {
                gui.errorDialogWindow("You are already disabled");
            }
        } catch (Exception e) {
            gui.errorDialogWindow("Message: an error occurred while disconnecting");
        }
    }
}

