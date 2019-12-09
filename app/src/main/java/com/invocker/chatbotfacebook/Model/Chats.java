package com.invocker.chatbotfacebook.Model;

public class Chats {
    private String receiver;
    private String messenger;
    private String sender;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Chats() {
    }

    public Chats(String receiver, String messenger, String sender) {
        this.receiver = receiver;
        this.messenger = messenger;
        this.sender = sender;
    }
}
