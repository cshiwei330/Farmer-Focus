package sg.edu.np.mad.madassignment1;

public class User {
    private static String UserName;
    private String Password;

    public User(){

    }

    // for menu header, need get username
    public static String getUsername() {
        return UserName;
    }

    public void setUsername(String username) {
        this.UserName = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }
}
