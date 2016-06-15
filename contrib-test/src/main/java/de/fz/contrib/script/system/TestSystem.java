package de.fz.contrib.script.system;

import com.artemis.BaseSystem;

/**
 * @author felixz
 */
public class TestSystem extends BaseSystem {

    @Override
    protected void processSystem() {
        // do nothing
    }

    public void someMethod() {
        System.out.println("some method in TestSystem");
    }
}
