
package apprepo;

public class Application {
    private String name;
    private String description;
    private String organization;
    private String platforms;
    private String[] links;
    private String price;
    private String status; // New field for status

    // Getters and setters
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    // Other getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganization() {
        return organization;
    }
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPlatforms() {
        return platforms;
    }
    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    public String[] getLinks() {
        return links;
    }
    public void setLinks(String[] links) {
        this.links = links;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
}
