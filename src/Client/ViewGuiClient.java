package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Set;

public class ViewGuiClient {
    private final Client client;
    private JFrame frame = new JFrame("ChatApp");
    private JTextArea messages = new JTextArea(30, 30);
    private JTextArea users = new JTextArea(30, 15);
    private JPanel panel = new JPanel();
    private JTextField textField = new JTextField(40);
    private JButton buttonDisable = new JButton("Disconnect");
    private JButton buttonConnect = new JButton("Connect");

    public ViewGuiClient(Client client){
        this.client = client;
    }

    protected void initFrameClient(){

        messages.setEditable(false);
        users.setEditable(false);
        frame.add(new JScrollPane(messages), BorderLayout.CENTER);
        frame.add(new JScrollPane(users), BorderLayout.EAST);
        panel.add(textField);
        panel.add(buttonConnect);
        panel.add(buttonDisable);
        frame.add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        buttonConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.connectToServer();
            }
        });

    }

    //додаємо текст до поля
    protected void addMessage(String text) {
        messages.append(text);
    }

    //оновлення списку імен під'єднаних користувачів
    protected void refreshListUsers(Set<String> listUsers) {
        users.setText("");
        if (client.isConnect()) {
            StringBuilder text = new StringBuilder("A list of users: \n");
            for (String user : listUsers) {
                text.append(user + "\n");
            }
            users.append(text.toString());
        }
    }

    //виклик вікна для вводу адреси сервера
    protected String getServerAddress(){
        while(true) {
            String addressServer = JOptionPane.showInputDialog(
                    frame, "Enter server address: ",
                    "Enter server address",
                    JOptionPane.QUESTION_MESSAGE
            );
            return addressServer.trim();
        }
    }

    //виклик вікна для вводу порта сервера
    protected int getPortServer(){
        while(true) {
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

    protected String getNameUser() {
        String name = JOptionPane.showInputDialog(
                frame, "Input user name: ",
                "Input User name",
                JOptionPane.QUESTION_MESSAGE
        );

        return name;
    }

    protected void errorDialogWindow(String text) {
        JOptionPane.showMessageDialog(
                frame, text, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
