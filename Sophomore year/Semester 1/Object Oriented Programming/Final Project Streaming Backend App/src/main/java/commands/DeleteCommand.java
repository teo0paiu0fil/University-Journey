package commands;

import mydatabase.MyDatabase;
import steams.Stream;
import users.Streamer;

public class DeleteCommand extends Command{
    private Streamer reciver;
    private String[] params;
    private MyDatabase db;
    private Stream prevDeleted;

    public DeleteCommand(Streamer reciver, String[] params, MyDatabase db) {
        this.reciver = reciver;
        this.params = params;
        this.db = db;
    }

    @Override
    public void execute() {
        prevDeleted = (Stream) db.getStreams().getElementByID(params[2]);
        reciver.deleteStream(params[2], db.getStreams());
    }

    @Override
    public void undo() {
        db.getStreams().addToData(String.valueOf(prevDeleted.getId()),prevDeleted);
    }
}
