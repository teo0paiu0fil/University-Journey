package commands;

public abstract class Command {
    public abstract void execute();
    public abstract void undo();
}
