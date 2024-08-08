package apprepo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
		configNextGen =  config; 
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
		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
List<Application> applications = new ArrayList<>();
        
        // Read data from the file
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
                    // Remove brackets and split by commas
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
        
        // Set applications attribute to be accessed in JSP
        //request.setAttribute("applications", applications);
        request.getSession().setAttribute("applications", applications);
        // Forward to JSP to display the data
        request.getRequestDispatcher("/displayApplications.jsp").forward(request, response);
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
        		// Retrieve form data
        		String externalLink = request.getParameter("externalLink");
        		// Process and store the external link (pseudo code)
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

        		// Save application (pseudo code)
        		saveApplication(app);

        		// Redirect or forward as necessary
        		response.sendRedirect("success.jsp");
	}

}

protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");
    if ("viewBalance".equals(action)) {
        // Assuming user authentication is done and user ID is stored in session
        String userId = (String) request.getSession().getAttribute("userId");
        // Fetch account balance from the database (pseudo code)
        double accountBalance = fetchAccountBalance(userId);
        // Set the account balance as a request attribute
        request.setAttribute("accountBalance", accountBalance);
        // Forward to the JSP page
        request.getRequestDispatcher("/accountBalance.jsp").forward(request, response);
    } else {
        // Fetch outstanding requests (pseudo code)
        List<Application> applications = fetchOutstandingRequests();
        // Set applications as request attribute
        request.setAttribute("applications", applications);
        // Forward to JSP page
        request.getRequestDispatcher("/outstandingRequests.jsp").forward(request, response);
    }
}

// Method to fetch outstanding requests (pseudo code)
private List<Application> fetchOutstandingRequests() {
    // Logic to fetch applications with status "standby" from the database
    return new ArrayList<>(); // Example return value
}

