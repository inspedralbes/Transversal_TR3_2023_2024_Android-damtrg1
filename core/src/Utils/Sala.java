package Utils;

import java.util.ArrayList;

public class Sala {
    String id;
    ArrayList<String> users;

    ArrayList<String> usuarisAtacantes;

    ArrayList<String> usuarisDefensores;

    String nombreMapa;

    public Sala(String id, String nombreMapa, ArrayList<String> atacantes, ArrayList<String> defensores) {
        this.id = id;
        this.nombreMapa = nombreMapa;
        this.usuarisAtacantes = atacantes;
        this.usuarisDefensores = defensores;
    }

    public Sala(String id,  ArrayList<String> atacantes, ArrayList<String> defensores) {
        this.id = id;
        this.usuarisAtacantes = atacantes;
        this.usuarisDefensores = defensores;
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

    public ArrayList<String> getUsuarisAtacantes() {
        return usuarisAtacantes;
    }

    public ArrayList<String> getUsuarisDefensores() {
        return usuarisDefensores;
    }
}
