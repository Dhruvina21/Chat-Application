package service;

import model.ChatMessage;
import model.ChatRoom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static constants.SQLQuries.*;
import static service.DbConnectionService.dbConnection;

public class ChatRoomOperations {


//    public List<ChatRoom> getAllChatRooms() {
//
//    }

//    public List<ChatRoom> getAllChatRoomsByUser() {
//
//    }


    public static Integer createChatRoom(String chatRoomName) {
        ///Need to put validation for chatRoomName
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(CREATE_CHATROOM);
            preparedStatement.setString(1, chatRoomName);
            System.out.println("Executing : " + preparedStatement);
            preparedStatement.executeUpdate();


            //Getting room Id for new created room

            ResultSet rs = dbConnection.prepareStatement("select roomid from public.chatroom where chatroom.roomname='" + chatRoomName + "'").executeQuery();

            Integer roomId;
            while (rs.next()) {
                roomId = rs.getInt("roomid");
                return joinChatRoomAndGetID(chatRoomName, roomId);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }


    public static Integer joinChatRoomAndGetID(String chatRoomName, Integer userID) {
        //Check if chat-room exists
        Boolean ifExists = checkIfChatRoomExists(chatRoomName);

        if (!ifExists) {
            return null;
        } else {
            System.out.println("Joining chatroom : " + chatRoomName);
            try {

                Statement statement = dbConnection.createStatement();
                ResultSet rs = statement.executeQuery("select roomid from public.chatroom where chatroom.roomname='" + chatRoomName + "'");

                while (rs.next()) {
                    int roomId = rs.getInt("roomid");
                    PreparedStatement preparedStatement = dbConnection.prepareStatement(INSERT_ONLINE_STATUS);
                    preparedStatement.setInt(1, roomId);
                    preparedStatement.setInt(2, userID);
                    System.out.println("Executing : " + preparedStatement);
                    preparedStatement.executeUpdate();

                    return roomId;
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Chat room does not exist : " + chatRoomName);
                return null;
            }
        }
        return null;
    }


    public static boolean checkIfChatRoomExists(String chatRoomName) {
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery("select * from public.chatroom where chatroom.roomname='" + chatRoomName + "'");
            return rs.next();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Chat room does not exist : " + chatRoomName);
            return false;
        }
    }


    public static void insertMessage(ChatMessage chatMessage) throws SQLException {
        if (chatMessage != null) {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(INSERT_MESSAGES);
            preparedStatement.setInt(1, chatMessage.getRoomId());
            preparedStatement.setInt(2, chatMessage.getUserId());
            preparedStatement.setString(3, chatMessage.getMessage());
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            System.out.println("Executing : " + preparedStatement);
            preparedStatement.executeUpdate();
            System.out.println("Chat message  : " + chatMessage.getMessage() + " Inserted successfully ");
        }

    }

    public static Boolean leaveRoom(Integer userId, Integer chatRoomId) {
        try {

            String leaveRoomQuery = getLeaveRoomQuery(userId, chatRoomId);
            System.out.println("Executing leave room query : " + leaveRoomQuery);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(leaveRoomQuery);
            System.out.println("Executing : " + preparedStatement);
            preparedStatement.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static List<ChatMessage> getAllMessageFromchatRoom(Integer userId) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(GET_ALL_MESSAGES);
            preparedStatement.setInt(1, userId);
            System.out.println("Executing : " + preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            return fetchFromResultSetChatRoom(resultSet);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<String> getOnlineUsersFromChatRoom(Integer roomId) {
        try {

            PreparedStatement preparedStatement = dbConnection.prepareStatement(CURRENT_USER_IN_ROOM);

            preparedStatement.setInt(1, roomId);
            System.out.println("Executing : " + preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<String> onlineUsers = new ArrayList<>();
            while (resultSet.next()) {
                onlineUsers.add(resultSet.getString("username"));
            }
            return onlineUsers;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    private static List<ChatMessage> fetchFromResultSetChatRoom(ResultSet resultSet) throws SQLException {
        List<ChatMessage> chatRooms = new ArrayList<>();
        while (resultSet.next()) {
            ChatMessage chatRoom = new ChatMessage(
                    resultSet.getInt("roomid"),
                    resultSet.getInt("userid"),
                    resultSet.getString("messages"),
                    resultSet.getTime("messagestime")
            );
            chatRooms.add(chatRoom);
        }
        return chatRooms;
    }


}
