package commands;

import mydatabase.MyDatabase;
import users.Streamer;

public class AddCommand extends Command{
    private Streamer reciver;
    private String[] params;
    private MyDatabase db;

    public AddCommand(Streamer reciver, String[] params, MyDatabase db) {
        this.reciver = reciver;
        this.params = params;
        this.db = db;
    }

    @Override
    public void execute() {
        reciver.addStream(params, db.getStreams());
    }

    @Override
    public void undo() {
        reciver.deleteStream(params[3], db.getStreams());
    }
}
