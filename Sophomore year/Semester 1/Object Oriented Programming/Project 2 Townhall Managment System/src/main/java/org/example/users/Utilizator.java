package org.example.users;

import org.example.Cerere;

import java.text.SimpleDateFormat;
import java.util.*;

public abstract class Utilizator {
    private String name;
    private ArrayList<Cerere> requestInWaiting;
    private ArrayList<Cerere> requestSolve;

    public Utilizator(String name) {
        this.name = name;
        this.requestInWaiting = new ArrayList<>();
        this.requestSolve = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addToWaiting(Cerere req) {
        requestInWaiting.add(req);
        Collections.sort(requestInWaiting);
    }

    public void addSolve(Cerere req) {
        requestSolve.add(req);
    }

    public ArrayList<Cerere> getRequestInWaiting() {
        return requestInWaiting;
    }

    public ArrayList<Cerere> getRequestSolve() {
        return requestSolve;
    }

    public abstract String writeRequest(Cerere.RequestType req);
    public abstract Cerere makeRequest(Cerere.RequestType req, int prio, Date date);
    public void deleteRequest(Date date) {
        requestInWaiting.removeIf((e) -> {
            return date.equals(e.getDate());
        });
    }

    public abstract String getClas();

}
