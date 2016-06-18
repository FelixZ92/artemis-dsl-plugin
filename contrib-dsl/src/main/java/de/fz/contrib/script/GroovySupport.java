package de.fz.contrib.script;

import com.artemis.ArtemisPlugin;
import com.artemis.WorldConfigurationBuilder;
import groovy.lang.GroovyShell;

/**
 * Plugin providing support for groovy-scripts with a custom DSL for artemis-odb.
 * Register with:
 * <code>{@link WorldConfigurationBuilder#with(ArtemisPlugin...)}</code>
 *
 * <p>For more information on available keywords, see {@link ArtemisScript}
 *
 * @author felixz
 */
public class GroovySupport implements ArtemisPlugin {

    /**
     *
     */
    private final GroovyShell shell;

    /**
     *
     */
    public GroovySupport() {
        this(new GroovyShell());
    }

    /**
     * @param groovyShell the GroovyShell to use
     */
    public GroovySupport(final GroovyShell groovyShell) {
        this.shell = groovyShell;
    }

    @Override
    public final void setup(final WorldConfigurationBuilder builder) {
        builder.with(new ScriptManager(shell));
    }
}
