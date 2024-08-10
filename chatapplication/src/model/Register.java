package model;

public class Register extends Login{
    public Register(String userName, String password) {
        super(userName, password);
    }
    public Register(Login login) {
        super(login.getUserName(), login.getPassword());
    }
}
