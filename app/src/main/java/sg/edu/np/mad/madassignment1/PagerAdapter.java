package sg.edu.np.mad.madassignment1;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    private String[] pageTitles = new String[]{"My Barn", "My Silo"};

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return BarnFragment.newInstance(0, pageTitles[0]);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return SiloFragment.newInstance(1, pageTitles[1]);
            default:
                return null;
        }
    }



    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position];
    }

}
