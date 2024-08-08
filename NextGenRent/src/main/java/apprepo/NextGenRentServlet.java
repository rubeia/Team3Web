
package apprepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/NextGenRentServlet")
public class NextGenRentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch outstanding requests
        List<Application> applications = fetchOutstandingRequests();
        // Set applications as request attribute
        request.setAttribute("applications", applications);
        // Forward to JSP page
        request.getRequestDispatcher("/outstandingRequests.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String appId = request.getParameter("appId");
        String comment = request.getParameter("comment");

        if ("Approve".equals(action)) {
            // Approve application with comment
            approveApplication(appId, comment);
        } else if ("Deny".equals(action)) {
            // Deny application with comment
            denyApplication(appId, comment);
        } else {
            // Retrieve form data
            String externalLink = request.getParameter("externalLink");
            // Process and store the external link
            storeExternalLink(externalLink);
            // Create application object
            Application app = new Application();
            app.setName(request.getParameter("name"));
            app.setDescription(request.getParameter("description"));
            app.setOrganization(request.getParameter("organization"));
            app.setPlatforms(request.getParameter("platforms"));
            app.setLinks(request.getParameterValues("links"));
            app.setPrice(request.getParameter("price"));
            app.setStatus("standby"); // Set status to standby

            // Save application
            saveApplication(app);

            // Redirect or forward as necessary
            response.sendRedirect("success.jsp");
        }
    }

    // Method to fetch outstanding requests
    private List<Application> fetchOutstandingRequests() {
        // Logic to fetch applications with status "standby" from the database
        return new ArrayList<>(); // Example return value
    }

    // Method to approve application with comment
    private void approveApplication(String appId, String comment) {
        updateApplicationStatus(appId, "approved", comment);
    }

    // Method to deny application with comment
    private void denyApplication(String appId, String comment) {
        updateApplicationStatus(appId, "denied", comment);
    }

    // Method to store the external link
    private void storeExternalLink(String externalLink) {
        // Logic to store the link in the database
    }

    // Method to save the application
    private void saveApplication(Application app) {
        // Logic to save application in the database
    }

    // Method to update application status with comment
    private void updateApplicationStatus(String appId, String status, String comment) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Connect to the database (replace with your database connection details)
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdatabase", "username", "password");

            // SQL query to update application status and add admin comment
            String sql = "UPDATE applications SET status = ?, admin_comment = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setString(2, comment);
            pstmt.setString(3, appId);

            // Execute the update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
