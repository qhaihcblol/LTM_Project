package Model.BO;

import Model.DAO.Authentication_DAO;

public class Authentication_BO {
    public boolean checkLogin(String username, String password) {
        Authentication_DAO checkLoginDAO = new Authentication_DAO();
        return checkLoginDAO.checkLogin(username, password);
    }
    public void addUser(String username, String password, String email) {
        Authentication_DAO checkLoginDAO = new Authentication_DAO();
        checkLoginDAO.addUser(username, password, email);
    }
    public boolean isExistUser(String username) {
        Authentication_DAO checkLoginDAO = new Authentication_DAO();
        return checkLoginDAO.isExistUser(username);
    }
    public boolean isExistEmail(String email) {
        Authentication_DAO checkLoginDAO = new Authentication_DAO();
        return checkLoginDAO.isExistEmail(email);
    }
    public boolean isPasswordMatching(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
