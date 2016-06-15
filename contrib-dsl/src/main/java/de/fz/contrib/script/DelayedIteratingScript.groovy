package de.fz.contrib.script

import com.artemis.World
import com.artemis.utils.IntBag

/**
 *
 * @author felixz
 */
abstract class DelayedIteratingScript extends BaseEntityScript {

    def remainingDelayClosure
    def timeDeltaClosure
    def processDeltaClosure
    def processExpiredClosure

    /** The time until an entity should be processed. */
    private float delay;
    /**    If the system is running and counting down delays. */
    private boolean running;
    /** The countdown, accumulates world deltas. */
    private float acc;

    @Override
    void setWorld(World world) {
        super.setWorld(world)
        timeDeltaClosure = { return this.world.getDelta() }
    }

    @Override
    protected final void processScript() {
        IntBag entities = subscription.getEntities();
        int processed = entities.size();
        if (processed == 0) {
            stop();
            return;
        }

        delay = Float.MAX_VALUE;
        int[] ids = entities.getData();
        for (int i = 0; processed > i; i++) {
            int e = ids[i];
            processDelta(e, acc);
            float remaining = getRemainingDelay(e);
            if (remaining <= 0) {
                processExpired(e);
            } else {
                offerDelay(remaining);
            }
        }
        acc = 0;
    }

    @Override
    protected void inserted(int entityId) {
        float remainingDelay = getRemainingDelay(entityId);
        processDelta(entityId, -acc);
        if (remainingDelay > 0) {
            offerDelay(remainingDelay);
        }

        super.inserted(entityId)
    }

    @Override
    void remainingDelay(Closure closure) {
        remainingDelayClosure = closure
    }

    float getRemainingDelay(int entity) {
        return remainingDelayClosure(entity)
    }

    @Override
    protected final boolean checkProcessing() {
        if (running) {
            acc += getTimeDelta();
            return acc >= delay;
        }
        return false;
    }

    void timeDelta(Closure closure) { timeDeltaClosure = closure }

    protected float getTimeDelta() {
        return timeDeltaClosure()
    }


    protected void processDelta(int entityId, float accumulatedDelta) {
        processDeltaClosure(entityId, accumulatedDelta)
    }

    protected void processExpired(int entityId) {
        processExpiredClosure(entityId)
    }

    void processDelta(Closure closure) { processDeltaClosure = closure }

    void processExpired(Closure closure) { processExpiredClosure = closure }

    void offerDelay(float offeredDelay) {
        if (!running) {
            running = true;
            delay = offeredDelay;
        } else {
            delay = Math.min(delay, offeredDelay);
        }
    }

    float getInitialTimeDelay() {
        return delay;
    }

    float getRemainingTimeUntilProcessing() {
        return running ? delay - acc : 0
    }

    boolean isRunning() {
        return running;
    }

    void stop() {
        this.running = false;
        this.acc = 0;
    }
}
