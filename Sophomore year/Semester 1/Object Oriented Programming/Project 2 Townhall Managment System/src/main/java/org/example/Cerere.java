package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cerere implements Comparable {
    public static enum RequestType {
        inlocuire_buletin ("inlocuire buletin"),
        inregistrare_venit_salarial ("inregistrare venit salarial"),
        inlocuire_carnet_de_sofer ("inlocuire carnet de sofer"),
        inlocuire_carnet_de_elev ("inlocuire carnet de elev"),
        creare_act_constitutiv ("creare act constitutiv"),
        reinnoire_autorizatie ("reinnoire autorizatie"),
        inregistrare_cupoane_de_pensie ("inregistrare cupoane de pensie");

        private final String body;

        RequestType(String body) {
            this.body = body;
        }

        public String getBody() {
            return body;
        }
    }

    private int priority;
    private RequestType req;
    private Date date;

    public Cerere(int priority, RequestType req, Date date) {
        this.priority = priority;
        this.req = req;
        this.date = date;
    }

    public int getPriority() {
        return priority;
    }

    public RequestType getReq() {
        return req;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(Object o) {
        return date.compareTo(((Cerere)o).getDate());
    }

}
