package Client;

import java.util.HashSet;
import java.util.Set;

public class ModelGuiClient {

    //в клієнтському додатку зберігаєм множину підключених користувачів
    private Set<String> users = new HashSet<>();

    protected void setUsers(Set<String> users) {
        this.users = users;
    }

    protected Set<String> getUsers() {
        return users;
    }

    protected void addUser (String nameUser) {
        users.add(nameUser);
    }

    protected void removeUser (String nameUser) {
        users.remove(nameUser);
    }

}
