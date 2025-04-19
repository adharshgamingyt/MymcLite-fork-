package me.supposedly_sam.mymcLite.Models;

public class GSItemClickModel {
    private final String displayName;
    private final String click;
    private final String command;

    public GSItemClickModel(String displayName, String click, String command) {
        this.displayName = displayName;
        this.click = click;
        this.command = command;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getClick() {
        return click;
    }

    public String getCommand() {
        return command;
    }
}
