<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="Header.jsp" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="Model.DAO.Database" %>
<html>
<head>
    <title>Lịch sử chuyển đổi</title>
    <link rel="stylesheet" href="CSS_File/History.css">
</head>
<body>
<div class="container">
    <a href="Upload.jsp" class="back-to-home">Back to Home</a> <h2>Lịch sử chuyển đổi</h2>
    <table>
        <thead>
        <tr>
            <th>File gốc</th>
            <th>File đã chuyển đổi</th>
            <th>Ngày chuyển đổi</th>
        </tr>
        </thead>
        <tbody>
        <%
            int userId = (int) session.getAttribute("userId");
            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            try {
                connection = Database.getInstance().getConnection();
                String query = "SELECT file_upload, file_converted, date FROM history WHERE user_id = ? ORDER BY date DESC";
                statement = connection.prepareStatement(query);
                statement.setInt(1, userId);
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
        %>
        <tr>
            <td><%= resultSet.getString("file_upload") %></td>
            <td><%= resultSet.getString("file_converted") %></td>
            <td><%= resultSet.getTimestamp("date") %></td>
        </tr>
        <%
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (resultSet != null) {
                    try { resultSet.close(); } catch (Exception e) { e.printStackTrace(); }
                }
                if (statement != null) {
                    try { statement.close(); } catch (Exception e) { e.printStackTrace(); }
                }
            }
        %>
        </tbody>
    </table>
</div>

</body>
</html>