package commands;

import mydatabase.MyDatabase;
import users.User;

public class ListenCommand extends Command {
    private User reciver;
    private String[] params;
    private MyDatabase db;

    public ListenCommand(User reciver, String[] params, MyDatabase db) {
        this.reciver = reciver;
        this.params = params;
        this.db = db;
    }

    @Override
    public void execute() {
        reciver.listen(params[2], db);
    }

    @Override
    public void undo() {
        // no action
    }
}
