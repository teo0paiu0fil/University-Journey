package com.example.project;

public class Tema1 {

	public static void main(final String[] args) {
		if (args == null) {
			System.out.println("Hello world!");
			return;
		}
		Application app = new Application();
		app.run(args);
	}
}
