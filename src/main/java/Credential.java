public class Credential {
    private int id;
    private String site;
    private String username;
    private String password;
    private String notes;

    public Credential(int id, String site, String username, String password, String notes) {
        this.id = id;
        this.site = site;
        this.username = username;
        this.password = password;
        this.notes = notes;
    }

    public int getId() { return id; }
    public String getSite() { return site; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNotes() { return notes; }

    public void setSite(String site) { this.site = site; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setNotes(String notes) { this.notes = notes; }
}