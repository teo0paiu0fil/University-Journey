package org.events;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.apache.commons.io.IOUtils;

import org.example.ManagementPrimarie;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.text.ParseException;

public class TestEvenimentePrimarie {

    String antetOutput = "src/main/resources/output/";
    String antetRef = "src/main/resources/references/";

    //@Test
    public void emptyOutput() {
        File filesList[] = new File(antetOutput).listFiles();
        for (File file : filesList) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                file.delete();
            }
        }
        assertTrue(true);
    }

    @Test
    public void addEvent() throws IOException {
        String file = "01_adauga_eveniment_1.txt";
        ManagementPrimarie.main(new String[]{file});
        Reader out = new BufferedReader(new FileReader(antetOutput + file));
        Reader ref = new BufferedReader(new FileReader(antetRef + file));
        assertTrue(IOUtils.contentEqualsIgnoreEOL(out, ref));
    }

    @Test
    public void deleteEvent() throws IOException {
        String file = "02_sterge_eveniment_1.txt";
        ManagementPrimarie.main(new String[]{file});
        Reader out = new BufferedReader(new FileReader(antetOutput + file));
        Reader ref = new BufferedReader(new FileReader(antetRef + file));
        assertTrue(IOUtils.contentEqualsIgnoreEOL(out, ref));
    }

    @Test
    public void showEventsMonth() throws IOException {
        String file = "03_afiseaza_evenimente_luna.txt";
        ManagementPrimarie.main(new String[]{file});
        Reader out = new BufferedReader(new FileReader(antetOutput + file));
        Reader ref = new BufferedReader(new FileReader(antetRef + file));
        assertTrue(IOUtils.contentEqualsIgnoreEOL(out, ref));
    }

    @Test
    public void changeEvent() throws IOException {
        String file = "04_modifica_eveniment.txt";
        ManagementPrimarie.main(new String[]{file});
        Reader out = new BufferedReader(new FileReader(antetOutput + file));
        Reader ref = new BufferedReader(new FileReader(antetRef + file));
        assertTrue(IOUtils.contentEqualsIgnoreEOL(out, ref));
    }

    @Test
    public void All() throws IOException {
        String file = "05_test_complex.txt";
        ManagementPrimarie.main(new String[]{file});
        Reader out = new BufferedReader(new FileReader(antetOutput + file));
        Reader ref = new BufferedReader(new FileReader(antetRef + file));
        assertTrue(IOUtils.contentEqualsIgnoreEOL(out, ref));
    }
}
