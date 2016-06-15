package de.fz.contrib.script

import com.artemis.utils.IntBag

/**
 *
 * @author felixz
 */
abstract class IntervalIteratingScript extends IntervalScript {

    @Override
    protected void processScript() {
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        int s = actives.size()
        for (int i = 0; s > i; i++) {
            processClosure(ids[i])
        }
    }
}
