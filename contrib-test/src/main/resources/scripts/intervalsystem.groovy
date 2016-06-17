package script

import de.fz.contrib.script.comp.Bar
import de.fz.contrib.script.comp.Baz
import de.fz.contrib.script.comp.Foo
import de.fz.contrib.script.system.TestSystem

/**
 *
 * @author felixz
 */

interval 1f

wire {
    wire TestSystem
    wire Foo
    wire Bar
    wire Baz
}


aspect {
    all(Foo, Baz, Bar)
}


process {
    println "processing intervalsystem.groovy"
}
