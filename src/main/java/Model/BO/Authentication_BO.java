package Model.BO;

import Model.DAO.Authentication_DAO;

public class Authentication_BO {
    public boolean checkLogin(String username, String password) {
        Authentication_DAO authenticationDao = new Authentication_DAO();
        return authenticationDao.checkLogin(username, password);
    }
    public void addUser(String username, String password, String email) {
        Authentication_DAO authenticationDao = new Authentication_DAO();
        authenticationDao.addUser(username, password, email);
    }
    public boolean isExistUser(String username) {
        Authentication_DAO authenticationDao = new Authentication_DAO();
        return authenticationDao.isExistUser(username);
    }
    public boolean isExistEmail(String email) {
        Authentication_DAO checkLoginDAO = new Authentication_DAO();
        return checkLoginDAO.isExistEmail(email);
    }
    public boolean isPasswordMatching(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
    public int getUserId(String username) {
        Authentication_DAO authenticationDao = new Authentication_DAO();
        return authenticationDao.getUserId(username);
    }
}
