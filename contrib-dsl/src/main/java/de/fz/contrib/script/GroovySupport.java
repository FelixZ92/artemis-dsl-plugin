package de.fz.contrib.script;

import com.artemis.ArtemisPlugin;
import com.artemis.WorldConfigurationBuilder;
import groovy.lang.GroovyShell;

/**
 * Plugin providing support for groovy-scripts with a custom DSL for artemis-odb. <br>
 * Register with:
 * <code>{@link WorldConfigurationBuilder#with(ArtemisPlugin...)}</code>
 * <p>
 * For more information on available keywords, see {@link ArtemisScript}
 *
 * @author felixz
 */
public class GroovySupport implements ArtemisPlugin {

    private final GroovyShell shell;

    public GroovySupport() {
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
