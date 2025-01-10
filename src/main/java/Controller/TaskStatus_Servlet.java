package Controller;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;

import Model.BO.FileConverter_BO;
import Model.Bean.Task;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "TaskStatus_Servlet", value = "/TaskStatus_Servlet")
public class TaskStatus_Servlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false); // Lấy session (không tạo mới nếu không tồn tại)
		if (session != null) {
			Integer userId = (Integer) session.getAttribute("userId");
			if (userId != null) {
				List<Task> tasks = FileConverter_BO.getTasksByUserId(userId.intValue());

				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");

				String json = new Gson().toJson(tasks);
				resp.getWriter().write(json);
			} else {
				// Xử lý trường hợp userId chưa được set (chưa đăng nhập)
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
				resp.getWriter().write("{\"error\": \"User not logged in\"}");
			}
		} else {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
			resp.getWriter().write("{\"error\": \"User not logged in\"}");
		}
	}
}