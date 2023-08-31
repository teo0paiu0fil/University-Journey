package com.example.project;

import java.io.*;
import java.util.ArrayList;

public class MyDatabase implements IDatabase {

    public ArrayList<String> read(String filename) {
        ArrayList<String> readList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = br.readLine();

            while (line != null) {
                readList.add(line);
                line = br.readLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return readList;
    }

    public void write(ArrayList<String> writeList, String filename) {
        try (FileWriter fw = new FileWriter(filename, false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
             for (String line : writeList) {
                out.println(line);
             }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reset(String[] filenames) {
        for (String filename: filenames) {
            try {
                new FileWriter(filename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
