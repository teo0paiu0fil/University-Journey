package commands;

import mydatabase.MyDatabase;
import steams.Stream;
import users.Client;

public class ListCommand extends Command {
    private Client reciver;
    private MyDatabase db;
    private Stream prevDeleted;


    public ListCommand(Client reciver, MyDatabase db) {
        this.reciver = reciver;
        this.db = db;
    }

    @Override
    public void execute() {
        reciver.showStreams(db);
    }

    @Override
    public void undo() {
        // no action
    }
}
