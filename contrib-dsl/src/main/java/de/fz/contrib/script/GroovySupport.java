package de.fz.contrib.script;

import com.artemis.ArtemisPlugin;
import com.artemis.WorldConfigurationBuilder;
import groovy.lang.GroovyShell;

/**
 * @author felixz
 */
public class GroovySupport implements ArtemisPlugin {

    private final GroovyShell shell;

    public GroovySupport(){
        this(new GroovyShell());
    }

    public GroovySupport(GroovyShell shell) {
        this.shell = shell;
    }

    @Override
    public void setup(WorldConfigurationBuilder b) {
        b.with(new ScriptManager(shell));
    }
}
