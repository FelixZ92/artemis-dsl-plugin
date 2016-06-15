package de.fz.contrib.script

import com.artemis.Aspect
import com.artemis.AspectSubscriptionManager
import com.artemis.Component
import com.artemis.EntitySubscription
import com.artemis.utils.IntBag


/**
 * Behaves similar to {@link com.artemis.BaseEntitySystem}
 *
 * Annotate a script with <code>@groovy.transform.BaseScript(BaseEntityScript)</code> to use this.
 *
 * Available functions in a script using this class:
 * <code>inserted</code>
 * <code>removed</code>
 * <code>aspect</code>
 * and all of {@link ArtemisScript}
 *
 * @author felixz
 */
abstract class BaseEntityScript extends ArtemisScript implements EntitySubscription.SubscriptionListener {

    protected def insertedClosure
    protected def removedClosure

    /**
     * @return entity subscription backing this system.
     */
    public EntitySubscription getSubscription() {
        final AspectSubscriptionManager sm = world.getAspectSubscriptionManager();
        return sm.get(aspectConfiguration);
    }

    /**
     * Gets the entities processed by this system. Do not delete entities from
     * this bag - it is the live thing.
     *
     * @return System's entity ids, as matched by aspect.
     */
    public IntBag getEntityIds() {
        return subscription.getEntities();
    }

    @Override
    void inserted(Closure closure) { insertedClosure = closure }

    protected void inserted(int entityId) { insertedClosure(entityId) }

    @Override
    void inserted(IntBag entities) {
        int[] ids = entities.getData();
        int s = entities.size()
        for (int i = 0; s > i; i++) {
            inserted(ids[i])
        }
    }

    @Override
    void removed(Closure closure) { removedClosure = closure }

    protected void removed(int entityId) { removedClosure(entityId) }

    @Override
    void removed(IntBag entities) {
        int[] ids = entities.getData();
        int s = entities.size()
        for (int i = 0; s > i; i++) {
            removed(ids[i]);
        }
    }

    @Override
    void aspect(@DelegatesTo(AspectDelegate) Closure closure) {
        if (aspectConfiguration != null)
            throw new RuntimeException("aspect may not be called twice!")
        def delegate = closure.rehydrate(new AspectDelegate(), this, this)
        delegate()

        subscription = getSubscription()
        subscription.addSubscriptionListener(this)
    }

    protected class AspectDelegate {

        void all() {
            if (aspectConfiguration == null) aspectConfiguration = Aspect.all()
            else aspectConfiguration.all()
        }

        void all(Class<? extends Component>... components) {
            if (aspectConfiguration == null) aspectConfiguration = Aspect.all(components)
            else aspectConfiguration.all(components)
        }

        void one(Class<? extends Component>... components) {
            if (aspectConfiguration == null) aspectConfiguration = Aspect.one(components)
            else aspectConfiguration.one(components)
        }

        void exclude(Class<? extends Component>... components) {
            if (aspectConfiguration == null) aspectConfiguration = Aspect.exclude(components)
            else aspectConfiguration.exclude(components)
        }
    }
}
