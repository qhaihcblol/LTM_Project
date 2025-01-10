package Controller;

import Model.BO.Authentication_BO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "Authentication_Servlet", value = "/Authentication_Servlet")
public class Authentication_Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("Logout".equals(action)) {
            if (req.getSession(false) != null) {
                req.getSession().invalidate(); // Hủy phiên
            }
            resp.sendRedirect("index.jsp"); // Chuyển hướng về trang index.jsp
            return;
        }

        if ("Login".equals(action)) {
            resp.sendRedirect("Login.jsp");
        } else if ("Register".equals(action)) {
            resp.sendRedirect("Register.jsp");
        } else {
            HttpSession session = req.getSession(false);
            if (session != null && session.getAttribute("LoggedIn") != null && (boolean) session.getAttribute("LoggedIn")) {
                resp.sendRedirect("Upload.jsp");
            } else {
                resp.sendRedirect("index.jsp"); // Nếu không có phiên hợp lệ, quay lại index.jsp
            }
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Authentication_BO authenticationBO = new Authentication_BO();
        String action = req.getParameter("action");

        if (action.equals("Login")) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            if (authenticationBO.checkLogin(username, password)) {
                int userId = authenticationBO.getUserId(username);
                req.getSession().setAttribute("userId", userId);
                req.getSession().setAttribute("username", username);
                req.getSession().setAttribute("LoggedIn", true);
                resp.sendRedirect("Upload.jsp");
            } else {
                req.setAttribute("username", username);
                req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
                req.getRequestDispatcher("Login.jsp").forward(req, resp);
            }

        } else if (action.equals("Register")) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String confirmPassword = req.getParameter("confirmPassword");
            String email = req.getParameter("email");

            if (!authenticationBO.isPasswordMatching(password, confirmPassword)) {
                req.setAttribute("username", username);
                req.setAttribute("email", email);
                req.setAttribute("error", "Mật khẩu không khớp");
                req.getRequestDispatcher("Register.jsp").forward(req, resp);
            } else if (authenticationBO.isExistUser(username)) {
                req.setAttribute("username", username);
                req.setAttribute("email", email);
                req.setAttribute("error", "Tên đăng nhập đã tồn tại");
                req.getRequestDispatcher("Register.jsp").forward(req, resp);
            } else if (authenticationBO.isExistEmail(email)) {
                req.setAttribute("username", username);
                req.setAttribute("email", email);
                req.setAttribute("error", "Email đã tồn tại");
                req.getRequestDispatcher("Register.jsp").forward(req, resp);
            } else {
                authenticationBO.addUser(username, password, email);
                resp.sendRedirect("Login.jsp");
            }
        }
    }
}
