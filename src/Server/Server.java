package Server;

import Connection.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;


public class Server {

    private ServerSocket serverSocket;
    private static ViewGuiServer gui;
    private static ModelGuiServer model;
    private static volatile boolean isServerStart = false; //прапор для стану сервера

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
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new ServerThread(socket).start();
            } catch (Exception e) {
                gui.refreshDialogWindowServer("The connection to the server has been lost.\n");
                break;
            }
        }
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

    //клас-потік, який запускається, коли сервер приймає сокет нового клієнта(передаємо сокет )
    private class ServerThread extends Thread {

        private Socket socket;

        public ServerThread (Socket socket) {
            this.socket = socket;
        }

        //метод запиту у клієнта ім'я та додавання в мапу сервером
        private String requestAndAddingUser (Connection connection) {
            while(true){
                try {
                    //посилаємо клієнту запит на ім'я
                    connection.send(new Message(MessageType.REQUEST_NAME_USER));
                    Message responceMessage = connection.receive();
                    String userName = responceMessage.getTextMessage();
                    //перевірка чи не зайняте ім'я
                    if(responceMessage.getTypeMessage() == MessageType.USER_NAME &&
                       userName != null && !userName.isEmpty() &&
                       !model.getAllUsersMultiChat().containsKey(userName)) {
                        //додаємо ім'я в мапу
                        model.addUser(userName, connection);
                        Set<String> listUsers = new HashSet<>();
                        for(Map.Entry<String, Connection> users : model.getAllUsersMultiChat().entrySet()) {
                            listUsers.add(users.getKey());
                        }
                        //відправляємо множину імен користувачів для клієнта
                        connection.send(new Message(MessageType.NAME_ACCEPTED, listUsers));
                        //відправляємо всім клієнтам повідомлення про нового користувача
                        sendMessageAllUsers(new Message(MessageType.USER_ADDED, userName));
                        return userName;
                    } else {
                        //якщо ім'я вже зайнято, надсилаємо повідомлення клієнту
                        connection.send(new Message(MessageType.NAME_USED));
                    }
                } catch (Exception e) {
                    gui.refreshDialogWindowServer("An error occurred while requesting and adding a new user\n");
                }
            }
        }

        //метод, який реалізує обмін повідомленнями між користувачами
        private void messagingBetweenUsers (Connection connection, String userName) {
            while (true) {
                try {
                    Message message = connection.receive();
                    //прийняли повідомлення клієнта
                    //якщо це текстове повідомлення, то надсилаємо всім
                    if (message.getTypeMessage() == MessageType.TEXT_MESSAGE) {
                        String textMessage = String.format("%s: %s\n", userName, message.getTextMessage());
                        sendMessageAllUsers(new Message(MessageType.TEXT_MESSAGE, textMessage));
                    }
                    //якщо клієнт відключився, надсилаєм користувачам повідомлення
                    //видаляємо з мапи та закриваємо connection
                    if (message.getTypeMessage() == MessageType.DISABLE_USER) {
                        sendMessageAllUsers(new Message(MessageType.USER_REMOVED, userName));
                        model.removeUser(userName);
                        connection.close();
                        gui.refreshDialogWindowServer(String.format("User %s has logged out.\n", socket.getRemoteSocketAddress()));
                        break;
                    }
                } catch (Exception e) {
                    gui.refreshDialogWindowServer(String.format("An error occurred while sending a message from user %s, or disconnected!\n", userName));
                    break;
                }
            }
        }

        @Override
        public void run(){
            gui.refreshDialogWindowServer(String.format("New user connected with remote socket - %s.\n", socket.getRemoteSocketAddress()));
            try {
                //отримуємо connection за допомогою сокета від клієнта та запитуємо ім'я
                //запускаємо цикл обміну повідомлень між клієнтами
                Connection connection = new Connection(socket);
                String nameUser = requestAndAddingUser(connection);
                messagingBetweenUsers(connection, nameUser);
            } catch (Exception e) {
                gui.refreshDialogWindowServer("An error occurred while sending a message from the user!\n");
            }
        }
    }
}