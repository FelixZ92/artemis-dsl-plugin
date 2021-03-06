package script

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

inserted { entity ->
    println "inserted in baseentitysystem.groovy $entity"
}

removed {
    entity ->
        println "removed in baseentitysystem.groovy $entity"
}
