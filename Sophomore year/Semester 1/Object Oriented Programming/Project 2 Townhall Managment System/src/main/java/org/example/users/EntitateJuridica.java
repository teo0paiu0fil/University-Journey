package org.example.users;

import org.example.Cerere;

import java.util.Date;

public class EntitateJuridica extends Utilizator {


    private String reprezentant;

    public EntitateJuridica(String name, String reprezentant) {
        super(name);
        this.reprezentant = reprezentant;
    }

    public String getReprezentant() {
        return reprezentant;
    }

    public String getName() {
        return super.getName();
    }

    @Override
    public String writeRequest(Cerere.RequestType req) {
        return "Subsemnatul " + getReprezentant() +
                ", reprezentant legal al companiei " +
                getName() +
                ", va rog sa-mi aprobati urmatoarea solicitare: " +
                req.getBody() + "\n";
    }

    @Override
    public Cerere makeRequest(Cerere.RequestType req, int prio, Date date) {
        return null;
    }

    @Override
    public String getClas() {
        return "entitate juridica";
    }
}
