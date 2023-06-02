package Server;

import javax.swing.*;
import java.awt.*;

public class ViewGuiServer {

    private final Server server;
    private JFrame frame = new JFrame("Server");
    private JTextArea dialogWindow = new JTextArea(10, 50);
    private JButton buttonStartServer = new JButton("Start server");
    private JButton buttonStopServer = new JButton("Stop server");
    private JPanel panelButtons = new JPanel();

    public ViewGuiServer(Server server) {
        this.server = server;
    }

    //метод ініціалізації інтерфейсу серверного додатку
    protected void initFrameServer() {
        dialogWindow.setEditable(false);
        dialogWindow.setLineWrap(true); //автоматичний перенос рядка
        frame.add(new JScrollPane(dialogWindow), BorderLayout.CENTER);
        panelButtons.add(buttonStartServer);
        panelButtons.add(buttonStopServer);
        frame.add(panelButtons, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }

    //метод, який додає повідомлення в текстове вікно
    public void refreshDialogWindowServer(String serviceMessage) {
        dialogWindow.append(serviceMessage);
    }
}
