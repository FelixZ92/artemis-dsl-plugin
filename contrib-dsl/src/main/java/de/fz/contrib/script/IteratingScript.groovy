package de.fz.contrib.script

import com.artemis.utils.IntBag
import groovy.transform.InheritConstructors

/**
 * Behaves similar to {@link com.artemis.systems.IteratingSystem}
 *
 * Annotate a script with <code>@groovy.transform.BaseScript(IteratingScript)</code> to use this.
 *
 * Available functions in a script using this class:
 * all of {@link BaseEntityScript} while <code>process</code> should be used like this:
 *
 * <code>
 *     process {
 *         entity -> do something with entity
 *     }
 * </code>
 *
 * @author felixz
 */
@InheritConstructors
abstract class IteratingScript extends BaseEntityScript {


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
