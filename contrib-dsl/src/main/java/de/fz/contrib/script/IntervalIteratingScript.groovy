package de.fz.contrib.script

import com.artemis.utils.IntBag

/**
 * Behaves similar to {@link com.artemis.systems.IntervalIteratingSystem}
 *
 * Annotate a script with <code>@groovy.transform.BaseScript(IntervalIteratingScript)</code> to use this.
 *
 * Available functions in a script using this class:
 * all of {@link IntervalScript}, while <code>process</code> should be used like in {@link IteratingScript}
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
