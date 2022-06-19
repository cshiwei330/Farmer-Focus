package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

public class DarkMode extends AppCompatActivity {
    public static final String PREFERENCES = "preferences";
    public static final String CUSTOM_THEME = "customeTheme";
    public static final String LIGHT_THEME = "lightTheme";
    public static final String  DARK_THEME = "darkTheme";

    private String customTheme;

    public String getCustomTheme()
    {
        return customTheme;
    }

    public void setCustomTheme(String customTheme)
    {
        this.customTheme = customTheme;
    }
}
