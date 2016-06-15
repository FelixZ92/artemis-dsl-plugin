package de.fz.contrib.script

import com.artemis.*

/**
 * Base class of a script using the DSL.
 * Behaves similar to {@link BaseSystem}
 *
 * Annotate a script with <code>@groovy.transform.BaseScript(ArtemisScript)</code> to use this.
 *
 * Available functions in a script using this class:
 * <code>wire</code>
 * <code>init</code>
 * <code>begin</code>
 * <code>process</code>
 * <code>checkProcessing</code>
 * <code>name</code>
 * <code>setEnabled</code>
 *
 * @author felixz
 */
abstract class ArtemisScript extends Script {

    protected World world;

    protected boolean enabled = true
    protected String name

    protected def initClosure
    protected def processClosure
    protected def beginClosure
    protected def endClosure
    protected def checkProcessingClosure

    // fields used in BaseEntityScript need to be declared here due to availability
    protected Aspect.Builder aspectConfiguration;
    protected EntitySubscription subscription

    ArtemisScript() {
        def mc = new ExpandoMetaClass(ArtemisScript, false, true)
        mc.initialize()
        this.metaClass = mc

        //default closures
        checkProcessing { true }
        begin {}
        end {}
        init {}
        process {}

        inserted {}
        removed {}
    }

    protected void name(String name) {
        this.name = name
    }

    protected void wire(@DelegatesTo(WireDelegate) Closure closure) {
        def wireDelegate = new WireDelegate(this)
        def doWire = closure.rehydrate(wireDelegate, this, this)
        doWire.resolveStrategy = Closure.DELEGATE_ONLY
        doWire()
    }

    final void process() {
        if (checkProcessing()) {
            begin()
            processScript()
            end()
        }
    }

    protected void begin(Closure closure) {
        beginClosure = closure
    }

    protected void begin() { beginClosure() }

    protected void process(Closure closure) {
        processClosure = closure
    }

    protected void processScript() { processClosure() }

    protected void end(Closure closure) {
        endClosure = closure
    }

    protected void end() { endClosure() }

    protected boolean checkProcessing(Closure closure) {
        checkProcessingClosure = closure
    }

    protected boolean checkProcessing() { checkProcessingClosure() }

    protected void init() { initClosure() }

    protected void init(Closure closure) {
        initClosure = closure
    }

    World getWorld() {
        return world
    }

    public void setWorld(World world) {
        this.world = world
    }

    boolean isEnabled() {
        return enabled
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled
    }

    String getName() {
        return name
    }

// closures used in de.fz.contrib.script extending subclasses of this base de.fz.contrib.script need to be declared here due to availability
    void aspect(Closure closure) {}

    void inserted(Closure closure) {}

    void removed(Closure closure) {}

    void interval(float interval) {}

    void remainingDelay(Closure closure) {}

    void timeDelta(Closure closure) {}

    void processDelta(Closure closure) {}

    void processExpired(Closure closure) {}

    protected class WireDelegate {

        private wireTo

        private WireDelegate(wireTo) {
            this.wireTo = wireTo
        }

        void wire(Class<?> classToWire) {
            def toWire
            def fieldName
            if (BaseSystem.isAssignableFrom(classToWire)) {
                toWire = world.getSystem(classToWire)
            } else if (Component.isAssignableFrom(classToWire)) {
                toWire = world.getMapper(classToWire)
            } else throw new RuntimeException("cannot wire $classToWire")

            //def toWire = BaseSystem.isAssignableFrom(classToWire) ? world.getSystem(classToWire)
            //        : Component.isAssignableFrom(classToWire) ? world.getMapper(classToWire) : null

 //           def name = classToWire.getSimpleName().toLowerCase()
            fieldName = toLowerCamelCase(classToWire.getSimpleName())
            wireTo.metaClass."$fieldName" = toWire
            println fieldName
        }
    }

    private static String toLowerCamelCase(String s) {
        if (s == null) return null
        char first = s.charAt(0);
        return s.replaceFirst(String.valueOf(first), String.valueOf(first.toLowerCase()))
    }

    protected class ProcessDelegate {

    }
}
