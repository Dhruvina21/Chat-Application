import model.ChatMessage;
import model.ChatRoom;
import model.Login;
import model.Register;
import service.ChatRoomOperations;
import service.DbConnectionService;
import service.DbOperations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ChatApplicationMain {

    public static void main(String[] args) {
        DbConnectionService.connectToDB();

        ChatRoomOperations.getAllMessageFromchatRoom(1);
        while (true) {

            System.out.println("Type 1 to Login ");
            System.out.println("Type 2 to Register");

            Scanner in = new Scanner(System.in);
            int input = in.nextInt();

            switch(input){
                case 1:
                    try{
                        login();
                    }
                    catch (Exception e){
                        System.out.println("Exception : Error in Login" + e.getMessage());
                    }
                    break;
                case 2:
                    try{
                        register();
                    }
                    catch(Exception e){
                        System.out.println("Exception : Error in Registration" + e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Please Enter Valid Number to Proceed");
            }
        }
    }

    public static void login() throws SQLException {

        System.out.println("--- Login Page ---");

        Scanner in = new Scanner(System.in);
        System.out.println("Please Enter Username: ");
        String username = in.nextLine();
        System.out.println("Please Enter Password: ");
        String password = in.nextLine();

        Integer userId=DbOperations.validateUser(new Login(username, password));
        if(userId!=null){
            goToMainView(userId);
        }
        else
            System.out.println("Invalid Username and Password !! Please try again");
    }

    public static void register() throws SQLException {
        System.out.println("--- Register Page ---");

        Scanner in = new Scanner(System.in);
        System.out.println("Please Enter Username: ");
        String username = in.nextLine();
        System.out.println("Please Enter Password: ");
        String password = in.nextLine();

        Integer userId=DbOperations.addUser(new Register(username, password));
        if(userId!=null){
            goToMainView(userId);
        }
        else{
            System.out.println("User Already Existed !!");
        }

    }

    public static void goToMainView(Integer userId)
    {
        System.out.println("--- Main View ---");
        boolean mainview = true;
        while(mainview){
            System.out.println("Type 1 to Join Chat Room ");
            System.out.println("Type 2 to Create Chat Room");
            System.out.println("Type 3 to Update Account Info");
            System.out.println("Type 4 to Logout");

            Scanner in = new Scanner(System.in);
            int input = in.nextInt();

            switch (input){
                case 1:
                    joinChatRoom(userId);
                    break;
                case 2:
                    createChatRoom(userId);
                    break;
                case 3:
                    updateAccountInfo(userId);
                    break;
                case 4:
                    logout();
                    mainview = false;
                    break;
                default:
                    System.out.println("Please Enter Valid Number to Proceed");
            }
        }
    }

    private static void logout() {
        System.out.println("Logging Out....");
    }

    private static void updateAccountInfo(Integer userId) {
        System.out.println("--- Update Account Info ---");

        Scanner in = new Scanner(System.in);
        System.out.println("Please Enter Updated Username: ");
        String username = in.nextLine();
        System.out.println("Please Enter Updated/Existing Password: ");
        String password = in.nextLine();

        if(Boolean.TRUE.equals(DbOperations.updateUser(username, password, userId))){
            System.out.println("Successfully updated the account information !!");
        }
        else{
            System.out.println("Error in Updating account information !!");
        }
    }

    private static void createChatRoom(Integer userId) {
        System.out.println("--- Create Chat Room ---");

        Scanner in = new Scanner(System.in);
        System.out.println("Please Enter Chatroom Name: ");
        String chatroom = in.nextLine();

        chatroom = chatroom.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        if(ChatRoomOperations.checkIfChatRoomExists(chatroom)){
            System.out.println("Chat room already exists !!");
        }
        else{
            Integer chatRoomId = ChatRoomOperations.createChatRoom(chatroom);
            if(chatRoomId != null){
                System.out.println("Chat Room id : " + chatRoomId);
                goToChatRoom(chatRoomId, userId);
            }
            else{
                System.out.println("Error in creating chatroom !!");
            }
        }
    }

    private static void joinChatRoom(Integer userId) {
        System.out.println("--- Join Chat Room ---");

        Scanner in = new Scanner(System.in);
        System.out.println("Please Enter Chatroom name: ");
        String chatroom = in.nextLine();

        Integer chatRoomId = ChatRoomOperations.joinChatRoomAndGetID(chatroom,userId);
        if(chatRoomId != null){
            System.out.println("Chat Room id -- "+chatRoomId);
           goToChatRoom(chatRoomId, userId);
        }
        else{
            System.out.println("Chat Room Does not exists !! Please try again.");
        }
    }

    private static void goToChatRoom(Integer chatRoomId, Integer userId){
        System.out.println("--- Chat Room ---");
        boolean leave = true;

        while(leave){
            System.out.println("Start Typing Messages : ");
            Scanner in = new Scanner(System.in);
            String messages = in.nextLine();
            char c = messages.charAt(0);

            switch (c){
                case '/':
                    if(messages.equals("/list")){
                        List<String> userList;
                        System.out.println("--- Online User List ---");
                        try{
                            userList = ChatRoomOperations.getOnlineUsersFromChatRoom(chatRoomId);
                            userList.forEach(uid -> {
                                System.out.println(uid);
                            });
                        }
                        catch(Exception e){
                            System.out.println("Exception in getting all users : " + e.getMessage());
                        }
                    }
                    else if(messages.equals("/leave")){
                        try{
                            ChatRoomOperations.leaveRoom(userId, chatRoomId);
                        }
                        catch (Exception e){
                            System.out.println("Exception in Leaving Room : " + e.getMessage());
                        }
                        leave = false;
                    }
                    else if(messages.equals("/history")){
                        System.out.println("--- Chat Hisotry ---");
                        List<ChatMessage> list;

                        try{
                            list = ChatRoomOperations.getAllMessageFromchatRoom(chatRoomId);
                            list.forEach(chatMessage -> {
                                System.out.println(chatMessage.getMessages() + " -- " + chatMessage.getMessagestime());
                            });
                        }
                        catch(Exception e){
                            System.out.println("Exception in getting history "  + e.getMessage());
                        }
                    }
                    else if(messages.equals("/help")){
                        System.out.println("List of Available Commands : ");
                        System.out.println("/list, /leave, /history, /help");
                    }
                    else{
                        System.out.println("Please Enter Valid Command !!");
                    }
                    break;
                default:
                    try{
                        ChatRoomOperations.insertMessage(new ChatMessage(chatRoomId, userId, messages));
                    }
                    catch (Exception e){
                        System.out.println("Error in Inserting Message : " + e.getMessage());
                    }
            }
        }
    }
}
