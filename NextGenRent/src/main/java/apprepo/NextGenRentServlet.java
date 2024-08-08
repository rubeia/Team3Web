package apprepo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NextGenRentServlet
 */
public class NextGenRentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    ServletConfig configNextGen;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public NextGenRentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Servlet#init(ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        configNextGen = config;
    }

    /**
     * @see Servlet#getServletConfig()
     */
    public ServletConfig getServletConfig() {
        return configNextGen;
    }

    /**
     * @see Servlet#getServletInfo()
     */
    public String getServletInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("outstandingRequests".equals(action)) {
            // Fetch outstanding requests
            List<Application> applications = fetchOutstandingRequests();
            // Set applications as request attribute
            request.setAttribute("applications", applications);
            // Forward to JSP page
            request.getRequestDispatcher("/outstandingRequests.jsp").forward(request, response);
        } else {
            // Default action: Read data from the file and display applications
            List<Application> applications = new ArrayList<>();
            String filePath = getServletContext().getRealPath("/data/applications.txt");
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                Application app = null;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("Name:")) {
                        app = new Application();
                        app.setName(line.substring(6).trim());
                    } else if (line.startsWith("Description:")) {
                        app.setDescription(line.substring(13).trim());
                    } else if (line.startsWith("Organization:")) {
                        app.setOrganization(line.substring(14).trim());
                    } else if (line.startsWith("Platforms:")) {
                        app.setPlatforms(line.substring(10).trim());
                    } else if (line.startsWith("Links:")) {
                        String linksStr = line.substring(7).trim();
                        String[] linksArray = linksStr.substring(1, linksStr.length() - 1).split(", ");
                        app.setLinks(linksArray);
                    } else if (line.startsWith("Price:")) {
                        app.setPrice(line.substring(7).trim());
                        applications.add(app);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            request.getSession().setAttribute("applications", applications);
            request.getRequestDispatcher("/displayApplications.jsp").forward(request, response);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
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
            // Existing POST logic
            // TODO Auto-generated method stub
            // doGet(request, response);
        }
    }

    private List<Application> fetchOutstandingRequests() {
        // Logic to fetch applications with status "standby" from the database
        return new ArrayList<>(); // Example return value
    }

    private void approveApplication(String appId, String comment) {
        updateApplicationStatus(appId, "approved", comment);
    }

    private void denyApplication(String appId, String comment) {
        updateApplicationStatus(appId, "denied", comment);
    }

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
