package de.fz.contrib.script;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.fz.contrib.script.system.TestDelayedSystem;
import de.fz.contrib.script.system.TestSystem;

import java.io.File;

/**
 * @author felixz
 */
public class ScriptTest extends ApplicationAdapter {

    private World world;

    public static void main(final String[] arg) {
        createApplication();
    }

    private static Application createApplication() {
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 600;
        return new LwjglApplication(new ScriptTest(), config);
    }

    @Override
    public void create() {
        WorldConfigurationBuilder builder = new WorldConfigurationBuilder();
        world = new World(
                builder.with(new GroovySupport())
                        .with(new TestSystem(), new TestDelayedSystem()).build()
        );

        ScriptManager scriptManager = world.getSystem(ScriptManager.class);
        scriptManager.registerScript(new File("scripts/basesystem.groovy"));
    }

    @Override
    public void render() {
        world.setDelta(Gdx.graphics.getDeltaTime());
        world.process();
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
