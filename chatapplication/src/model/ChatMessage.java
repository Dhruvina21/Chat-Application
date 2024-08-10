package model;

import java.util.Date;

public class ChatMessage {

    private Integer roomId;
    private Integer userId;
    private String message;

    private String messages;

    private Date messagestime;

    public ChatMessage(Integer roomId, Integer userId, String messages, Date messagestime) {
        this.roomId = roomId;
        this.userId = userId;
        this.messages = messages;
        this.messagestime = messagestime;
    }

    public ChatMessage(Integer roomId, Integer userId, String message) {
        this.roomId = roomId;
        this.userId = userId;
        this.message = message;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Date getMessagestime() {
        return messagestime;
    }

    public void setMessagestime(Date messagestime) {
        this.messagestime = messagestime;
    }
}
