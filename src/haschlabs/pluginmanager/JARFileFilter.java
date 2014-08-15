
package haschlabs.pluginmanager;

import java.io.File;
import java.io.FileFilter;

/**
 * Implementation of the FileFilter interface that accepts only .jar files.
 *
 * @author Hauke Schulz <hauke27@googlemail.com>
 */
public class JARFileFilter implements FileFilter {

    /**
     * This method checks if the given file object ends with .jar and returns
     * a boolean value.
     *
     * @author Hauke Schulz <hauke27@googlemail.com>
     * @param file A java.io.File instance
     * @return bool true if the file ends with .jar
     */
    @Override
    public boolean accept(File file) {
        return file.getName().toLowerCase().endsWith(".jar");
    }
}