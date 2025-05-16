package me.supposedly_sam.mymcLite.Models;

public class BanMessage {
    public String banText;
    public String durationText;
    public String reasonText;

    public BanMessage(String banText, String durationText, String reasonText) {
        this.banText = banText;
        this.durationText = durationText;
        this.reasonText = reasonText;
    }
}
