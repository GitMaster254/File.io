package Models;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String salt;

    public User(int id, String username, String password, String email, String salt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.salt = salt;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail(){
        return email;
    }

    public String getSalt(){
        return salt;
    }
    
}