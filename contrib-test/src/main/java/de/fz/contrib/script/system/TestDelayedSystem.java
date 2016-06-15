package de.fz.contrib.script.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.DelayedIteratingSystem;
import de.fz.contrib.script.comp.Bar;
import de.fz.contrib.script.comp.Baz;
import de.fz.contrib.script.comp.Foo;

/**
 * @author felixz
 */
public class TestDelayedSystem extends DelayedIteratingSystem {

    ComponentMapper<Foo> mFoo;
    ComponentMapper<Bar> mBar;
    ComponentMapper<Baz> mBaz;

    public TestDelayedSystem() {
        super(Aspect.all(Foo.class, Bar.class, Baz.class));
    }

    @Override
    protected void initialize() {
        for (int i = 0; i < 10; i++) {
            int id = world.create();
            mFoo.create(id);
            mBar.create(id);
            mBaz.create(id);
        }
    }

    @Override
    protected float getRemainingDelay(int entityId) {
        return 1f;
    }

    @Override
    protected void processDelta(int entityId, float accumulatedDelta) {
        System.out.println("processing " + entityId + "\tacc = " + accumulatedDelta);
    }

    @Override
    protected void processExpired(int entityId) {
        System.out.println(entityId + " expired, renew");
        offerDelay(entityId);
    }
}
