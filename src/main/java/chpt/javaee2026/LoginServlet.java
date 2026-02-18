package chpt.javaee2026;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.RequestDispatcher;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;



@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private boolean isUserFound = false;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login.html");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userLogin = request.getParameter("login").trim();
        String userPassword = request.getParameter("password").trim();

        PrintWriter pw = response.getWriter();

        try {
            pw.println("Get JDBC driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Document document = new Document(PageSize.A4, 25, 25, 25, 25);

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

            PdfWriter.getInstance(document, new FileOutputStream("D:\\1.pdf"));
            document.open();
            document.add(new Paragraph("Group PS4-1, Alex Smetanin, Database user list",
                            FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD,
                            new CMYKColor(0, 100, 100, 0))));

            PdfPTable t = new PdfPTable(7);
            t.setSpacingBefore(25);
            t.setSpacingAfter(25);
            PdfPCell c1 = new PdfPCell(new Phrase("ID"));
            t.addCell(c1);
            PdfPCell c2 = new PdfPCell(new Phrase("FirstName"));
            t.addCell(c2);
            PdfPCell c3 = new PdfPCell(new Phrase("LastName"));
            t.addCell(c3);
            PdfPCell c4 = new PdfPCell(new Phrase("UserName"));
            t.addCell(c4);
            PdfPCell c5 = new PdfPCell(new Phrase("Password"));
            t.addCell(c5);
            PdfPCell c6 = new PdfPCell(new Phrase("Location"));
            t.addCell(c6);
            PdfPCell c7 = new PdfPCell(new Phrase("Gender"));
            t.addCell(c7);

            Integer id;
            String firstName, lastName, userName, password, location, gender;

            int i=0;
            while (rs.next()) {
                pw.print(++i + ". ");
                id = rs.getInt("idusers");
                firstName = rs.getString("firstname");
                lastName = rs.getString("lastname");
                userName = rs.getString("username");
                password = rs.getString("password");
                location = rs.getString("location");
                gender = rs.getString("gender");

                t.addCell(id.toString());
                t.addCell(firstName);
                t.addCell(lastName);
                t.addCell(userName);
                t.addCell(password);
                t.addCell(location);
                t.addCell(gender);

                pw.println(userName +" "+password);

                if (userLogin.equals(rs.getString("username")) &&
                        userPassword.equals(rs.getString("password"))) {
                    isUserFound = true;
                    //break;
                }
            }

            stmt.close();
            document.add(t);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } finally {
            document.close();
        }

        if (isUserFound) {
            pw.println("User found!!!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/welcome.jsp");
            dispatcher.forward(request, response);
        } else {
            pw.println("User not found!!!");
        }
    }
}
