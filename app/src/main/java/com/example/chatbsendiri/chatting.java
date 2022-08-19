package com.example.chatbsendiri;

public class chatting {

    private String nickname;
    private String message ;
    private String room ;


    public chatting(){

    }
    public chatting(String nickname, String message, String room) {
        this.nickname = nickname;
        this.message = message;
        this.room = room;
    }

    public chatting(String message) {
        this.message = message;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String message) {
        this.room = room;
    }
}
