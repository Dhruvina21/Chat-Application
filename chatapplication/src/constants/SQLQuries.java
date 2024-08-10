package constants;

public class SQLQuries {

    private SQLQuries() {
    }

    public static final String CREATE_CHATROOM = "INSERT INTO public.chatroom (roomname) VALUES (?);";
    public static final String INSERT_USERS_SQL = "INSERT INTO public.userdata (username,userpassword) VALUES (?, ?);";

    public static final String INSERT_ONLINE_STATUS = "insert into public.onlinestatus (roomid,userid) values (?,?)";
    public static final String INSERT_MESSAGES = "insert into public.messages (roomid,userid,messages,messagestime) values (?,?,?,?)";

    private static final String LEAVE_ROOM = "delete from public.onlinestatus where roomid=:roomID and userid=:userID;";

    public static String getLeaveRoomQuery(Integer userId, Integer roomId) {
        return LEAVE_ROOM.replace(":roomID", String.valueOf(roomId)).replace(":userID", String.valueOf(userId));
    }

    //Get all messages form particular chat room in desc order
    public static String GET_ALL_MESSAGES = "select * from public.messages where roomid=? order by messagestime desc";

    public static String CURRENT_USER_IN_ROOM = "select distinct username from public.userdata as ud join public.onlinestatus as os on ud.userid=os.userid where os.roomid=?";

    public static String UPDATE_USER="update public.userData set username=? , userpassword=? where userData.userid=?;";
}
