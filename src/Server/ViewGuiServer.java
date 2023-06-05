package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        //коли зачиняється вікно додатку сервера
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                server.stopServer();
                System.exit(0);
            }
        });

        frame.setVisible(true);

        buttonStartServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int port = getPortServer();
                server.startServer(port);
            }
        });

        buttonStopServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.stopServer();
            }
        });
    }

    //метод, який додає повідомлення в текстове вікно
    public void refreshDialogWindowServer(String serviceMessage) {
        dialogWindow.append(serviceMessage);
    }

    protected int getPortServer() {
        while (true) {
            String port = JOptionPane.showInputDialog(
                    frame, "Enter server port: ",
                    "Enter server port",
                    JOptionPane.QUESTION_MESSAGE
            );

            try {
                return Integer.parseInt(port.trim());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        frame, "Incorrect server port entered. Try again",
                        "Server port input error", JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
