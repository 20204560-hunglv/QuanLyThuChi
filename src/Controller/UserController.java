package Controller;

import services.UserService;

public class UserController {
    UserService us = new UserService();
    public boolean checkInput(String field__one, String field__two, String field_three){
        if(field__one.equals("")) return true;
        if(field__two.equals("")) return true;
        if(field_three.equals("")) return true;
        return false;
    }
    public boolean checkInput(String username, String password){
        if(username.equals("")) return true;
        if(password.equals("")) return true;
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
    public boolean signIn(String username, String password){
        return us.signIn(username, password);
    }
    
    //Change pass
    public boolean changPass(String matkhaumoi, int idTaiKhoan){
        return us.changePass(matkhaumoi, idTaiKhoan);
    }
    
    public void deleteTK(int idtaikhoan){
        us.deleteTaiKhoan(idtaikhoan);
    }
}
