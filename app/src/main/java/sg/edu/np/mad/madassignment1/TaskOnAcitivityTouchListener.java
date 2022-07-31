package sg.edu.np.mad.madassignment1;

import android.view.MotionEvent;


/*----------------------------------------------------------------------------------------------------*/

                            /* TaskOnActivityTouchListener Interface */
/* This Interface is required for the TaskRecyclerTouchListener to work. */

/*----------------------------------------------------------------------------------------------------*/

public interface TaskOnAcitivityTouchListener {
    void getTouchCoordinates(MotionEvent ev);
}
