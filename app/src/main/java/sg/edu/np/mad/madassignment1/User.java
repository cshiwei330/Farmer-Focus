package sg.edu.np.mad.madassignment1;

public class User {
    private String UserName;
    private String Password;
    private Integer ImageID;

    private int userID;

    public User(){

    }

    public User(String userName, String password, Integer imageID, int userID){
        this.UserName = userName;
        this.Password = password;
        this.ImageID = imageID;
        this.userID = userID;
    }

    // for menu header, need get username
    public String getUsername() { return UserName; }

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


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

}
