package org.example.users;

import org.example.Cerere;

import java.util.Date;

public class Persoana extends Utilizator {

    public Persoana(String name) {
        super(name);
    }

    @Override
    public String writeRequest(Cerere.RequestType req) {
        return "Subsemnatul " + getName() +
                ", va rog sa-mi aprobati urmatoarea solicitare: " +
                req.getBody() + "\n";
    }

    @Override
    public Cerere makeRequest(Cerere.RequestType req, int prio, Date date) {
        return null;
    }

    @Override
    public String getClas() {
        return "persoana";
    }
}
