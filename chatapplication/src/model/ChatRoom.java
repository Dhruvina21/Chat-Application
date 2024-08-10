package model;

public class ChatRoom {
  
  private String roomid;
  private String roomname;

    public ChatRoom() {
    }

    public ChatRoom(String roomid, String roomname) {
        this.roomid = roomid;
        this.roomname = roomname;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }
}
