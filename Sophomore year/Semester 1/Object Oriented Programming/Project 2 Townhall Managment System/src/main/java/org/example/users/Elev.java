package org.example.users;

import org.example.Cerere;

import java.util.Date;

public class Elev extends Utilizator {

    private String scoala;

    public Elev(String name, String scoala) {
        super(name);
        this.scoala = scoala;
    }

    public String getName() {
        return super.getName();
    }

    public String getScoala() {
        return scoala;
    }

    @Override
    public String writeRequest(Cerere.RequestType req) {
        return "Subsemnatul " + getName() +
                ", elev la scoala " +
                getScoala() +
                ", va rog sa-mi aprobati urmatoarea solicitare: " +
                req.getBody() + "\n";
    }

    @Override
    public Cerere makeRequest(Cerere.RequestType req, int prio, Date date) {
        return new Cerere(prio, req, date);
    }

    @Override
    public String getClas() {
        return "elev";
    }
}
