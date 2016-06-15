package scripts

@groovy.transform.BaseScript(IteratingScript)
import de.fz.contrib.script.IteratingScript
import de.fz.contrib.script.comp.Bar
import de.fz.contrib.script.comp.Baz
import de.fz.contrib.script.comp.Foo
import de.fz.contrib.script.system.TestSystem

/**
 *
 * @author felixz
 */
wire {
    wire TestSystem
    wire Foo
    wire Bar
    wire Baz
}

init {
    10.times {
        id = world.create()
        bar.create(id)
        foo.create(id)
        baz.create(id)
    }
}

aspect {
    all(Foo, Baz, Bar)
}

process {
    entity -> println "processing $entity in iteratingsystem.groovy"
}
