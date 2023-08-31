package org.example;

import org.example.users.Utilizator;

public class SetOfTwo<K extends Cerere, T extends Utilizator> implements Comparable {
    private K object1;
    private T object2;

    public SetOfTwo(K object1, T object2) {
        this.object1 = object1;
        this.object2 = object2;
    }

    public K getObject1() {
        return object1;
    }

    public T getObject2() {
        return object2;
    }

    @Override
    public int compareTo(Object o) {
        if (object1.getPriority() > ((SetOfTwo<?, ?>)o).getObject1().getPriority())
            return -1;
        else if (object1.getPriority() < ((SetOfTwo<?, ?>)o).getObject1().getPriority())
            return 1;
        else return object1.getDate().compareTo(((SetOfTwo<?, ?>)o).getObject1().getDate());
    }
}
