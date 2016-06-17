package de.fz.contrib.script

import groovy.transform.InheritConstructors

/**
 * Behaves similar to {@link com.artemis.systems.IntervalSystem}
 *
 * Annotate a script with <code>@groovy.transform.BaseScript(IntervalScript)</code> to use this.
 *
 * Available functions in a script using this class:
 * <code>interval</code>
 * <code>timeDelta</code>
 * and all of {@link BaseEntityScript}
 *
 * @author felixz
 */

@InheritConstructors
abstract class IntervalScript extends BaseEntityScript {

    def timeDeltaClosure

    /** Accumulated delta to keep track of interval. */
    protected float acc;
    /** How long to wait between updates. */
    private float interval;

    private float intervalDelta;

    IntervalScript() {
        timeDeltaClosure = { return this.world.getDelta() }
    }

    @Override
    void interval(float interval) {
        this.interval = interval
    }

    @Override
    protected boolean checkProcessing() {
        acc += getTimeDelta();
        if (acc >= interval) {
            acc -= interval;
            intervalDelta = (acc - intervalDelta);
            return true;
        }
        return false;
    }

    /**
     * Gets the actual delta since this system was last processed.
     *
     * @return Time passed since last process round.
     */
    protected float getIntervalDelta() {
        return interval + intervalDelta;
    }

    void timeDelta(Closure closure) { timeDeltaClosure = closure }

    protected float getTimeDelta() {
        return timeDeltaClosure()
    }
}
