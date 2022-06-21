package sg.edu.np.mad.madassignment1;

public class User {
    private static String UserName;
    private String Password;
    private Integer ImageID;


    public User(){

    }

    public User(String userName, String password, Integer imageID){
        this.UserName = userName;
        this.Password = password;
        this.ImageID = imageID;
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

    public Integer getImageID() {return ImageID; }

    public void setImageID(Integer imageID) { this.ImageID = imageID; }
}
