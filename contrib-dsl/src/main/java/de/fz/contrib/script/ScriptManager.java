package de.fz.contrib.script;

import com.artemis.BaseSystem;
import de.fz.contrib.script.utils.FileUtils;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Register and retrieve your scripts with this manager.
 * <p>
 * If you don't call <code>setEnabled false</code> in a script, <br>
 * its process method will be called here.
 *
 * @author felixz
 */
public class ScriptManager extends BaseSystem {

    /**
     * This systems tag.
     */
    public static final String TAG = ScriptManager.class.getSimpleName();

    /**
     * The registered scripts mapped by their classes.
     */
    private final Map<Class<? extends ScriptAdapter>, ScriptAdapter> scriptsByClass;

    /**
     * The registered scripts mapped by their names.
     */
    private final Map<String, ScriptAdapter> scriptsByName;

    /**
     * The GroovyShell to use.
     */
    private GroovyShell groovyShell;

    /**
     * Constructs a new ScriptManager.
     */
    public ScriptManager() {
        this(null);
    }

    /**
     * Constructs a new ScriptManager.
     * Can be registered with a {@link com.artemis.World} directly or via {@link GroovySupport}
     *
     * @param shell the {@link GroovyShell} to use
     */
    public ScriptManager(final GroovyShell shell) {
        this.groovyShell = shell != null ? shell : new GroovyShell();
        this.scriptsByClass = new HashMap<>();
        this.scriptsByName = new HashMap<>();
    }

    /**
     * Register a {@link ArtemisScript}.
     * Does not work for scripts within the classpath,
     * Use {@link ScriptManager#registerScriptInternal(String)} instead.
     *
     * @param file the file containing a ArtemisScript
     * @param <T>  the explicit script type
     * @return an instance of the parsed script
     */
    @SuppressWarnings("unchecked")
    public final <T extends ScriptAdapter> T registerScript(final File file) {
        try {
            T script = (T) groovyShell.parse(file);
            this.registerScript(script);
            return script;
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
        return null;
    }

    /**
     * Register a script file on the classpath.
     *
     * @param fileName the script's file name
     * @param <T>      the explicit script type
     * @return an instance of the parsed script
     */
    @SuppressWarnings("unchecked")
    public final <T extends ScriptAdapter> T registerScriptInternal(final String fileName) {
        T script = (T) this.groovyShell.parse(FileUtils.readInternalAsString(fileName), fileName);
        this.registerScript(script);
        return script;
    }

    /**
     * Register an instance of {@link ScriptAdapter} directly.
     *
     * @param script the script to register
     * @param <T>    the explicit script type
     */
    public final <T extends ScriptAdapter> void registerScript(final T script) {
        if (!this.scriptsByClass.containsKey(script.getClass())) {
            script.setWorld(this.world);
            script.init();
            this.scriptsByClass.put(script.getClass(), script);
            if (script.getName() != null) {
                this.scriptsByName.put(script.getName(), script);
            }
        }
    }

    /**
     * Retrieve a registered {@link ScriptAdapter} by its class.
     *
     * @param scriptClass the class of the script to look up
     * @param <T>         the explicit script type
     * @return the registered script instance
     */
    @SuppressWarnings("unchecked")
    public final <T extends ScriptAdapter> T getScript(final Class<T> scriptClass) {
        return (T) this.scriptsByClass.get(scriptClass);
    }

    /**
     * Retrieve a registered {@link ScriptAdapter} by its name.
     * Only possible if <code>name {scriptname}</code> is called within a script
     *
     * @param name the scripts name
     * @param <T>  the explicit script type
     * @return the registered script instance
     */
    @SuppressWarnings("unchecked")
    public final <T extends ScriptAdapter> T getScript(final String name) {
        return (T) this.scriptsByName.get(name);
    }

    @Override
    protected final void processSystem() {
        for (ScriptAdapter script : this.scriptsByClass.values()) {
            if (script.isEnabled()) {
                script.process();
            }
        }
    }
}
