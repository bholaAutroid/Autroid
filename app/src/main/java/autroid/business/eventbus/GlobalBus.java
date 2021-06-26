package autroid.business.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by pranav.mittal on 02/01/18.
 */

public class GlobalBus {
    private static EventBus sBus;
    public static EventBus getBus() {
        if (sBus == null) sBus = EventBus.getDefault();
        return sBus;
    }

}
