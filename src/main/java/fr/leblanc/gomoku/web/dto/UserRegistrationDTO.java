package fr.leblanc.gomoku.web.dto;

public class UserRegistrationDTO
{
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    
    public UserRegistrationDTO() {
    }
    
    public UserRegistrationDTO(final String firstName, final String lastName, final String username, final String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
}