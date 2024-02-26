import commands.*;
import mydatabase.CollectionDecorator;
import mydatabase.MyDatabase;
import users.Client;
import users.Streamer;
import users.User;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ProiectPOO {

    private MyDatabase db;
    public ProiectPOO(String[] args) {
        this.db = MyDatabase.getInstance(args);
    }

    public static void main(String[] args) {
        if(args == null) {
            System.out.println("Nothing to read here");
        } else {
            ProiectPOO app = new ProiectPOO(args);

            try {
                Scanner scanner = new Scanner(new File("src/main/resources/" + args[3]));

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] aux = line.split(" ", 7);
                    app.execute(aux);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            app.db.resetCollections();
        }

    }

    public void execute(String[] aux) {

        Client user = checkUser(aux[0]);
        if(user == null) return;

        Command command;

        switch (aux[1]) {
            case "LIST":
                command = new ListCommand(user, db);
                break;
            case "ADD":
                command = new AddCommand((Streamer) user, aux, db);
                break;
            case "DELETE":
                command = new DeleteCommand((Streamer) user, aux, db);
                break;
            case "LISTEN":
                command = new ListenCommand((User) user, aux ,db);
                break;
            case "RECOMMEND":
                command = new RecommandCommand((User) user, aux ,db);
                break;
            case "SURPRISE":
                command = new SurpriseCommand((User) user, aux ,db);
                break;
            default:
                throw new IllegalArgumentException("Invalid Command");
        }
        command.execute();
    }

    private Client checkUser(String aux) {
        CollectionDecorator usersCollection = db.getUsers();
        CollectionDecorator streamersCollection = db.getStreamers();

        Client client = (Client) usersCollection.getElementByID(aux);
        if (client == null)
            client = (Client) streamersCollection.getElementByID(aux);
        return client;
    }
}
