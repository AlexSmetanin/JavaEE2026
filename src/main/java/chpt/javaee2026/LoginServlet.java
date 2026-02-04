package chpt.javaee2026;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;



@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private boolean isUserFound = false;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userLogin = request.getParameter("login").trim();
        String userPassword = request.getParameter("password").trim();
        String userFirstName, userLastName;

        PrintWriter pw = response.getWriter();

        try {
            pw.println("Get JDBC driver...");
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            pw.println("Connecting to database...");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/java_ee_db",
                    "root", "!QAZxsw2");

            pw.println("Creating statment...");
            Statement stmt = conn.createStatement();
            pw.println("Getting result from SQL...");
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            pw.println("Start user search...");
            int i=0;
            while (rs.next()) {
                pw.println(++i);
                pw.println(rs.getString("username")+" "+rs.getString("password"));

                if (userLogin.equals(rs.getString("username")) &&
                        userPassword.equals(rs.getString("password"))) {
                    userFirstName = rs.getString("firstname");
                    userLastName = rs.getString("lastname");
                    isUserFound = true;
                    break;
                }

            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (isUserFound) {
            pw.println("User found!!!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/welcome.jsp");
            dispatcher.forward(request, response);
        } else {
            pw.println("User not found!!!");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login.html");
        dispatcher.forward(request, response);
    }
}
