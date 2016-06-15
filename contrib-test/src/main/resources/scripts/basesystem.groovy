package script

@groovy.transform.BaseScript(ArtemisScript)
import de.fz.contrib.script.ArtemisScript
import de.fz.contrib.script.system.TestSystem

/**
 *
 * @author felixz
 */

wire {
    wire TestSystem
}

init {
    println "init......................."
}

process {
    println "processing basesystem.groovy"
    println "calling TestSystem from basesystem.groovy"
    testSystem.someMethod()
}

begin { println "begin... basesystem.groovy" }
end { println "end...basesystem.groovy" }