package org.example;

import org.example.users.Utilizator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.example.ManagementPrimarie.dateFormat;

public class Birou<T extends Utilizator> {
    private HashMap<String, FunctionarPublic> functionarPublici;
    private PriorityQueue<SetOfTwo<Cerere,T>> coadaCereri;

    public Birou() {
        this.functionarPublici = new HashMap<>();
        this.coadaCereri = new PriorityQueue<>();
    }

    public void displayRequest(FileWriter fw) {
        PriorityQueue<SetOfTwo<Cerere,T>> a = new PriorityQueue<>(coadaCereri);
        try {
            SetOfTwo<Cerere, T> set;
            while (!coadaCereri.isEmpty()) {
                set = coadaCereri.poll();
                fw.write((set.getObject1()).getPriority() + " - " + dateFormat.format((set.getObject1()).getDate()) + " - ");
                fw.write((set.getObject2()).writeRequest((set.getObject1()).getReq()));
            }
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
        coadaCereri = a;
    }

    public void add(T user, Cerere cerere) {
        coadaCereri.add(new SetOfTwo<>(cerere, user));
    }

    public void addFunctionar(String parameter) {
        functionarPublici.put(parameter, new FunctionarPublic(parameter));
    }

    public String solve(String parameter) {
        FunctionarPublic fp = functionarPublici.get(parameter);
        SetOfTwo<Cerere, T> set= coadaCereri.poll();
        assert set != null;
        fp.solve(set.getObject1(), set.getObject2());
        return dateFormat.format(set.getObject1().getDate()) + " - " + set.getObject2().getName() + "\n";
    }

    public void removeReq(Date date) {
        PriorityQueue<SetOfTwo<Cerere,T>> a = new PriorityQueue<>();
        SetOfTwo<Cerere, T> set;
        while (!coadaCereri.isEmpty()) {
            set = coadaCereri.poll();
            if(!date.equals(set.getObject1().getDate()))
                a.add(set);
        }
        coadaCereri = a;
    }
}
