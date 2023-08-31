package org.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;

public class Eveniment implements Comparable{
    private String organizator;
    private String name;
    private Date eventStart;
    private Date eventEnd;
    private int month;

    @Override
    public int compareTo(Object o) {
        return this.eventStart.compareTo(((Eveniment)o).eventStart);
    }

    public Eveniment(String organizator, String name, Date eventStart, Date eventEnd) {
        this.organizator = organizator;
        this.name = name;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        Calendar cal = Calendar.getInstance();
        cal.setTime(eventStart);
        this.month = cal.get(Calendar.MONTH);
    }

    public static String displayEventMonth(TreeSet<Eveniment> events, SimpleDateFormat dateFormat, String parameter) {
        StringBuilder sb = new StringBuilder("Evenimentele din luna : ");
        int luna = Integer.parseInt(parameter) ;
        sb.append(luna == 1 ? "Ianuarie" : (luna == 2) ? "Februarie" : (luna == 3) ? "Martie" :
                (luna == 4) ? "Aprilie" : (luna == 5) ? "Mai" : (luna == 6) ? "Iunie" :
                        (luna == 7) ? "Iulie" : (luna == 8) ? "August " : (luna == 9) ? "Septembrie"
                                : (luna == 10) ? "Octombrie" : (luna == 11) ? "Noiembrie" : (luna == 12) ? "Decembrie" : "INVALID");
        sb.append("\n");
        luna--;
        int finalLuna = luna;
        events.forEach((e) -> {
            if (e.getMonth() == finalLuna) {
                sb.append(e.displayEvent(dateFormat));
                sb.append("\n");
            }
        });
        return sb.toString();
    }

    private int getMonth() {
        return month;
    }

    private String displayEvent(SimpleDateFormat format) {
        return "{\n'eveniment' : '" + name + "',\n" +
                "'organizator' : '" + organizator + "',\n" +
                "'dataInceput' : '" + format.format(eventStart) + "',\n" +
                "'dataSfarsit' : '" + format.format(eventEnd) + "'\n" +
                "}";
    }

    public static String displayAll(TreeSet<Eveniment> ArbEvent, SimpleDateFormat format) {
        StringBuilder sb = new StringBuilder();
        for (Eveniment e: ArbEvent) {
            sb.append(e.displayEvent(format));
            sb.append(",\n");
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void change(Date start, Date end) {
        this.eventStart = start;
        this.eventEnd = end;
    }
}
