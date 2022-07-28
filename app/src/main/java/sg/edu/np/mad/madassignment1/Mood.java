package sg.edu.np.mad.madassignment1;

public class Mood {
    private String date;
    private String mood;
    private int moodUserID;

    public Mood (){
    }

    public Mood (String date, String mood, int moodUserID){
        this.date = date;
        this.mood = mood;
        this.moodUserID = moodUserID;
    }

    public String getDate (){ return date; }
    public String getMood (){ return mood; }

    public int getMoodUserID() {
        return moodUserID;
    }
    public void setMoodUserID(int moodUserID) {
        this.moodUserID = moodUserID;
    }

    public void changeMood (String newMood){
        this.mood = newMood;
    }
}
