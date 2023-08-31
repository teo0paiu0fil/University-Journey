package com.example.project;

import java.util.ArrayList;

public interface IDatabase {
    void write(ArrayList<String> list, String filename);
    ArrayList<String> read(String filename);
    void reset(String[] filenames);
}
