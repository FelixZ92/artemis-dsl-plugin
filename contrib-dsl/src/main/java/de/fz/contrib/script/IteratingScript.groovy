package de.fz.contrib.script

import com.artemis.utils.IntBag
import groovy.transform.InheritConstructors

/**
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
