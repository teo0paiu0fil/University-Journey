package commands;

import mydatabase.MyDatabase;
import users.User;

public class SurpriseCommand extends Command {

    private User reciver;
    private String[] params;
    private MyDatabase db;
    public SurpriseCommand(User user, String[] aux, MyDatabase db) {
        this.reciver = user;
        this.params = aux;
        this.db = db;
    }

    @Override
    public void execute() {
        int type = 0;
        switch (params[2]) {
            case "SONG":
                type = 1;
                break;
            case "AUDIOBOOK":
                type = 3;
                break;
            case "PODCAST":
                type = 2;
                break;
        }
        reciver.recommand3(type ,db);
    }

    @Override
    public void undo() {
        // no action
    }
}
