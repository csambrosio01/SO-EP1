package scheduler;

import ioDatas.Log;
import java.io.IOException;

public class Scheduler {

    private Log logger;

    public Scheduler() throws IOException {
        logger = Log.getInstance();
    }
}
