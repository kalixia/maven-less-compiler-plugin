package com.kalixia.maven.plugins.less;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import java.io.File;
import java.util.List;

/**
 * @goal compile
 * @phase generate-resources
 */
public class LessMojo extends AbstractMojo {
    /**
     * @parameter
     */
    private List<File> includes;

    /**
     * @parameter
     */
    private File outputDirectory;

    public void execute() throws MojoExecutionException, MojoFailureException {
        LessEngine engine = new LessEngine();
        for (File lessFile : includes) {
            getLog().info("Compiling LESS file " + lessFile.getPath());
            try {
                engine.compile(lessFile);
            } catch (LessException e) {
                throw new MojoExecutionException("Could not compile LESS file " + lessFile.getPath(), e);
            }
        }
    }
}
