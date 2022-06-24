package sg.edu.np.mad.madassignment1;

public class Mood {
    private String date;
    private String mood;

    public Mood (){
    }

    public Mood (String date, String mood){
        this.date = date;
        this.mood = mood;
    }

    public String getDate (){ return date; }
    public String getMood (){ return mood; }

    public void changeMood (String newMood){
        this.mood = newMood;
    }
}
