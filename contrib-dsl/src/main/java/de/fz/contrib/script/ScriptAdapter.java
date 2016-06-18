package de.fz.contrib.script;

import com.artemis.World;

/**
 * Abstraction Layer between Java- and Groovy-code.
 * Use this to access a script managed by {@link ScriptManager}
 *
 * @author felixz
 */
public interface ScriptAdapter {

    /**
     * Initializes the script. Gets called upon registration in {@link ScriptManager}.
     *
     * @see com.artemis.BaseSystem#initialize()
     */
    void init();

    /**
     * Returns the name of the script or null if not set.
     *
     * @return the script's name
     */
    String getName();

    /**
     * Enables or disables the script. It will only be processed if enabled is true
     *
     * @param enabled whether to enable or disable the script
     */
    void setEnabled(boolean enabled);

    /**
     * @return the enabled flag within a system
     * @see com.artemis.BaseSystem#isEnabled()
     */
    boolean isEnabled();

    /**
     * Processes the script.
     */
    void process();

    /**
     * @return the world this script runs on
     */
    World getWorld();

    /**
     *
     * @param world the world this script should run on
     */
    void setWorld(World world);
}
