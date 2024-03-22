package Utils;

import java.util.ArrayList;

public class Sala {
    String id;
    ArrayList<String> users;

    String nombreMapa;

    public Sala(String id, ArrayList<String> users, String nombreMapa) {
        this.id = id;
        this.users = users;
        this.nombreMapa = nombreMapa;
    }

    public Sala(String id, ArrayList<String> users) {
        this.id = id;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public String getNombreMapa() {
        return nombreMapa;
    }

    public void setNombreMapa(String nombreMapa) {
        this.nombreMapa = nombreMapa;
    }
}
