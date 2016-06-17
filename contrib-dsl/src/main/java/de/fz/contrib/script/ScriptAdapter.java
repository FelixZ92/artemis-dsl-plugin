package de.fz.contrib.script;

import com.artemis.World;

/**
 * Abstraction Layer between Java- and Groovy-code.
 * Use this to access a script managed by {@link ScriptManager}
 *
 * @author felixz
 */
public interface ScriptAdapter {

    void init();

    String getName();

    void setEnabled(boolean enabled);

    boolean isEnabled();

    void process();

    World getWorld();

    void setWorld(World world);
}
