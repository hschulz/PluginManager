
package haschlabs.pluginmanager.interfaces;

import java.util.EventListener;

/**
 * Defines the standard interface for a plugin using only basic methods.
 *
 * @author Hauke Schulz <hauke27@googlemail.com>
 */
public interface Plugin extends EventListener {

    /**
     * Initialize the plugin.
     *
     * @return false on any error while starting.
     */
    public boolean start();

    /**
     * Stop the plugin.
     *
     * @return False on any error while stopping.
     */
    public boolean stop();

    /**
     * Returns the PluginManager for the plugin.
     *
     * @return A Class that implements the PluginManager interface.
     */
    public PluginManager getPluginManager();

    /**
     * Sets the PluginManager for this plugin.
     *
     * @param manager A class that implements the PluginManager interface.
     */
    public void setPluginManager(PluginManager manager);

    /**
     * Returns the name of the plugin.
     *
     * @return The plugins name
     */
    public String getName();

    /**
     * Returns the description for the plugin.
     *
     * @return The plugins description
     */
    public String getDescription();

    /**
     * Returns the author information for the plugin.
     *
     * @return A string array containing formatted strings like
     * "FirstName LastName <email@provider>"
     */
    public String[] getAuthors();

    /**
     * Returns the version of the plugin.
     *
     * @return The plugins version
     */
    public String getVersion();

    /**
     * Returns the version of the application the plugin was written for.
     *
     * @return The targeted applications version.
     */
    public String getApplicationVersion();
}
