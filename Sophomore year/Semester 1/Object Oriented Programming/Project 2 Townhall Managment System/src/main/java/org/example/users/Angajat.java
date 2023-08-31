package org.example.users;

import org.example.Cerere;

import java.util.Date;

public class Angajat extends Utilizator {
    private String companie;

    public Angajat(String name, String companie) {
        super(name);
        this.companie = companie;
    }

    public String getCompanie() {
        return companie;
    }

    public String getName() {
        return super.getName();
    }

    @Override
    public String writeRequest(Cerere.RequestType req) {
        return "Subsemnatul " + getName() +
                ", angajat la compania " +
                getCompanie() +
                ", va rog sa-mi aprobati urmatoarea solicitare: " +
                req.getBody() + "\n";
    }

    @Override
    public Cerere makeRequest(Cerere.RequestType req, int prio, Date date) {
        return null;
    }

    @Override
    public String getClas() {
        return "angajat";
    }
}
