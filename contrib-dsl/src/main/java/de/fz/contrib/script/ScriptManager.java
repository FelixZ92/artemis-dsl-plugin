package de.fz.contrib.script;

import com.artemis.BaseSystem;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author felixz
 */
public class ScriptManager extends BaseSystem {

    public static final String TAG = ScriptManager.class.getSimpleName();

    private final Map<Class<? extends ArtemisScript>, ArtemisScript> scriptsByClass;
    private GroovyShell groovyShell;

    public ScriptManager() {
        this(null);
    }

    public ScriptManager(GroovyShell shell) {
        this.groovyShell = shell != null ? shell : new GroovyShell();
        this.scriptsByClass = new HashMap<>();
    }

    @Override
    protected void initialize() {

    }

    @SuppressWarnings("unchecked")
    public <T extends ArtemisScript> T registerScript(File file) {
        try {
            T script = (T) groovyShell.parse(file);
            script.setWorld(this.world);
            script.run();
            script.init();
            this.scriptsByClass.put(script.getClass(), script);
            return script;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T extends ArtemisScript> void registerScript(T script) {
        if (!this.scriptsByClass.containsKey(script.getClass())) {
            this.scriptsByClass.put(script.getClass(), script);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends ArtemisScript> T getScript(Class<T> scriptClass) {
        return (T) this.scriptsByClass.get(scriptClass);
    }

    @Override
    protected void processSystem() {
        for (ArtemisScript script : this.scriptsByClass.values()) {
            if (script.isEnabled()) script.process();
        }
    }
}
