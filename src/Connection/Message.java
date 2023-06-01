package Connection;

import java.io.Serializable;
import java.util.Set;

public class Message implements Serializable{
    private MessageType typeMessage; //тип повідомлення
    private String textMessage; //текст повідомлення
    private Set<String> listUsers; //множина імен користувачів у чаті
    public Message(MessageType typeMessage, String textMessage, Set<String> listUsers){
        this.typeMessage = typeMessage;
        this.textMessage = textMessage;
        this.listUsers = listUsers;
    }
    public Message(MessageType typeMessage, String textMessage){
        this.typeMessage = typeMessage;
        this.textMessage = textMessage;
        this.listUsers = null;
    }
    public Message(MessageType typeMessage) {
        this.typeMessage = typeMessage;
        this.textMessage = null;
        this.listUsers = null;
    }
    public MessageType getTypeMessage(){
        return typeMessage;
    }
    public String getTextMessage(){
        return textMessage;
    }
    public Set<String> getListUsers(){
        return listUsers;
    }
}
