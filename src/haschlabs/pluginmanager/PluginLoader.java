
package haschlabs.pluginmanager;

import haschlabs.pluginmanager.interfaces.Plugin;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 *
 * @author Hauke Schulz <hauke27@googlemail.com>
 */
public class PluginLoader {

    /**
     * The default folder containing the plugins.
     */
    public static final String DEFAULT_FOLDER = "plugins";

    /**
     * The folder containing the plugins.
     */
    protected File pluginFolder;

    /**
     * All .jar files found in the plugin folder.
     */
    protected File[] plugJars;

    /**
     * The filenames converted to URLs.
     */
    protected URL[] urls;

    /**
     * Used to load the classes inside the jar files.
     */
    protected ClassLoader cl;

    /**
     * A list containing all classes that extend the plugin interface.
     */
    private List<Class<? extends Plugin>> plugClasses;

    /**
     * A list containing the actual plugin instances.
     */
    private List<Plugin> plugs;

    /**
     * Creates a new instance of the PluginLoader with the given folder
     * to look for plugins.
     *
     * @author Hauke Schulz <hauke27@googlemail.com>
     * @param pluginFolder
     */
    public PluginLoader(File pluginFolder) {
        this.pluginFolder   = pluginFolder;
        this.plugJars       = this.pluginFolder.listFiles(new JARFileFilter());
        this.urls           = new URL[this.plugJars.length];
        this.cl             = null;
        this.plugClasses    = new ArrayList<>();
        this.plugs          = new ArrayList<>();
    }

    /**
     * Loads all plugins from the specified folder and returns a list of
     * plugin instances.
     *
     * @author Hauke Schulz <hauke27@googlemail.com>
     * @return A list of class instances implementing the Plugin interface.
     * @throws IOException
     */
    public List<? extends Plugin> loadPlugins() throws IOException {

        this.fileArrayToURLArray();

        this.extractClassesFromJARs();

        this.createPluginObjects();

        return this.plugs;
    }

    /**
     * Copys the jar files path as an URL to the url array and initializes the
     * classloader with it.
     *
     * @author Hauke Schulz <hauke27@googlemail.com>
     * @throws MalformedURLException In case a path can't be converted.
     */
    protected void fileArrayToURLArray() throws MalformedURLException {

        for (int i = 0; i < this.plugJars.length; i++) {
            this.urls[i] = this.plugJars[i].toURI().toURL();
        }

        this.cl = new URLClassLoader(this.urls);
    }

    /**
     * Extracts the classes from the jar files.
     *
     * @author Hauke Schulz <hauke27@googlemail.com>
     * @throws IOException If the jar file can't be accessed
     */
    protected void extractClassesFromJARs() throws IOException {

        for (File jar : this.plugJars) {
            this.extractClassesFromJAR(jar);
        }
    }

    /**
     *
     * @author Hauke Schulz <hauke27@googlemail.com>
     * @param jar
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected void extractClassesFromJAR(File jar) throws IOException {

        JarInputStream jaris = new JarInputStream(
                new FileInputStream(jar)
        );

        JarEntry ent = null;

        while ((ent = jaris.getNextJarEntry()) != null) {

            if (ent.getName().toLowerCase().endsWith(".class")) {

                try {
                    Class<?> cls = this.cl.loadClass(
                        ent.getName()
                            .substring(0, ent.getName().length() - 6)
                                .replace('/', '.')
                    );

                    if (this.isPluginClass(cls)) {
                        this.plugClasses.add((Class<? extends Plugin>) cls);
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Can't load Class " + ent.getName());
                    e.printStackTrace(System.err);
                }
            }
        }

        jaris.close();
    }

    /**
     *
     *
     * @author Hauke Schulz <hauke27@googlemail.com>
     * @param cls
     * @return
     */
    protected boolean isPluginClass(Class<?> cls) {
        try {
            if (cls.newInstance() instanceof Plugin) {
                return true;
            }
        }  catch (InstantiationException | IllegalAccessException ex) {
            System.err.println(ex.toString());
        }

        return false;
    }

    /**
     *
     *
     * @author Hauke Schulz <hauke27@googlemail.com>
     */
    protected void createPluginObjects() {

        this.plugs = new ArrayList<>(this.plugClasses.size());

        for (Class<? extends Plugin> plug : this.plugClasses) {
            try {
                this.plugs.add(plug.newInstance());
            } catch (InstantiationException e) {
                System.err.println("Can't instantiate plugin: " + plug.getName());
            } catch (IllegalAccessException e) {
                System.err.println("IllegalAccess for plugin: " + plug.getName());
            }
        }
    }
}
