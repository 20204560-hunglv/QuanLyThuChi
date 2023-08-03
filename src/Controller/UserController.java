package Controller;

import quanlythuchi.services.UserService;

public class UserController {
    UserService us = new UserService();
    public boolean checkInput(String username, String password, String passCofirm){
        if(username.equals("")) return true;
        if(password.equals("")) return true;
        if(passCofirm.equals("")) return true;
        return false;
    }
    public boolean checkPass(String pass, String passCofirm){
        if(!pass.equals(passCofirm)) return true;
        return false;
    }
    public boolean checkTaiKhoan(String username){
        return us.checkTaiKhoan(username);
    }
    public boolean create(String username,String password){
        return us.save(username, password);
    }
}
