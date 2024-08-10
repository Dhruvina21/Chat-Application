package constants;

public class DbConstants {

   private DbConstants(){

   }

   public static final String DB_URL="jdbc:postgresql://localhost:5432/";
   public static final String DB=DB_URL.concat("postgres");
   public static final String USER_TABLE="";
   public static final String CHAT_ROOM_TABLE="";
   public static final String MESSAGE_TABLE="";

   public static final String DB_DRIVER="org.postgresql.Driver";
   public static final String USER="postgres";
   public static final String PASSWORD="";

}
