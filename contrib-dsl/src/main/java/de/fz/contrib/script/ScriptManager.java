package de.fz.contrib.script;

import com.artemis.BaseSystem;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Register and retrieve your scripts with this manager.
 * <p>
 * If you don't call <code>setEnabled false</code> in a script, its process method will be called here.
 *
 * @author felixz
 */
public class ScriptManager extends BaseSystem {

    public static final String TAG = ScriptManager.class.getSimpleName();

    private final Map<Class<? extends ArtemisScript>, ArtemisScript> scriptsByClass;
    private final Map<String, ArtemisScript> scriptsByName;
    private GroovyShell groovyShell;

    public ScriptManager() {
        this(null);
    }

    /**
     * Constructs a new ScriptManager.
     * Can be registered with a {@link com.artemis.World} directly or via {@link GroovySupport}
     *
     * @param shell
     */
    public ScriptManager(GroovyShell shell) {
        this.groovyShell = shell != null ? shell : new GroovyShell();
        this.scriptsByClass = new HashMap<>();
        this.scriptsByName = new HashMap<>();
    }

    /**
     * Register a {@link ArtemisScript}.
     *
     * @param file the file containing a ArtemisScript
     * @return an instance of the parsed script
     */
    @SuppressWarnings("unchecked")
    public <T extends ArtemisScript> T registerScript(File file) {
        try {
            T script = (T) groovyShell.parse(file);
            script.setWorld(this.world);
            script.run();
            script.init();
            this.scriptsByClass.put(script.getClass(), script);
            if (script.getName() != null) this.scriptsByName.put(script.getName(), script);
            return script;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Register an instance of {@link ArtemisScript} directly
     *
     * @param script the script to register
     */
    public <T extends ArtemisScript> void registerScript(T script) {
        if (!this.scriptsByClass.containsKey(script.getClass())) {
            this.scriptsByClass.put(script.getClass(), script);
            if (script.getName() != null) this.scriptsByName.put(script.getName(), script);
        }
    }

    /**
     * Retrieve a registered {@link ArtemisScript} by its class
     *
     * @param scriptClass the class of the script to look up
     * @return the registered script instance
     */
    @SuppressWarnings("unchecked")
    public <T extends ArtemisScript> T getScript(Class<T> scriptClass) {
        return (T) this.scriptsByClass.get(scriptClass);
    }

    /**
     * Retrieve a registered {@link ArtemisScript} by its name.
     * Only possible if <code>name {scriptname}</code> is called within a script
     *
     * @param name the scripts name
     * @return the registered script instance
     */
    @SuppressWarnings("unchecked")
    public <T extends ArtemisScript> T getScript(String name) {
        return (T) this.scriptsByName.get(name);
    }

    @Override
    protected void processSystem() {
        for (ArtemisScript script : this.scriptsByClass.values()) {
            if (script.isEnabled()) script.process();
        }
    }
}
