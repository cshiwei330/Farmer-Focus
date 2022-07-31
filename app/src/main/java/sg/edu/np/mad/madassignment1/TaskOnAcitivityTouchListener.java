/*----------------------------------------------------------------------------------------------------*/

                        /* TaskOnActivityTouchListener Interface */
/* This Interface is required for the TaskRecyclerTouchListener to work. */

/*----------------------------------------------------------------------------------------------------*/

package sg.edu.np.mad.madassignment1;

import android.view.MotionEvent;

public interface TaskOnAcitivityTouchListener {
    void getTouchCoordinates(MotionEvent ev);
}
