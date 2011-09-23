package com.kalixia.maven.plugins.less;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessException;
import org.apache.maven.model.PatternSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @goal compile
 * @phase generate-resources
 */
public class LessMojo extends AbstractMojo {
    /**
     * A set of file patterns to include LESS files
     *
     * @parameter
     */
    private PatternSet includes;

    /**
     * @parameter expression="${project.build.directory}/${project.build.finalName}"
     */
    private File outputDirectory;

    /**
     * @parameter expression="${basedir}/src/main/webapp"
     */
    private File webappSourceDirectory;

    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException, MojoFailureException {
        LessEngine engine = new LessEngine();
        try {
            List<File> lessFiles;
            if (includes != null) {
                lessFiles = FileUtils.getFiles(
                        webappSourceDirectory,
                        getCommaSeparatedList(includes.getIncludes()),
                        getCommaSeparatedList(includes.getExcludes())
                );
            } else {
                lessFiles = FileUtils.getFiles(webappSourceDirectory, "**/*.less", "");
            }
            for (File lessFile : lessFiles) {
                String cssFileLocation = lessFile.getPath().replace(".less", ".css").replace(webappSourceDirectory.getPath(), "");
                File cssFile = new File(outputDirectory, cssFileLocation);
                if (!cssFile.getParentFile().exists())
                    cssFile.getParentFile().mkdirs();
                getLog().info("Compiling LESS file " + lessFile.getPath() + " into " + cssFile.getPath());
                try {
                    engine.compile(lessFile, cssFile);
                } catch (LessException e) {
                    throw new MojoExecutionException("Could not compile LESS file " + lessFile.getPath(), e);
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Could not figure out which files to include/exclude", e);
        }
    }

    protected String getCommaSeparatedList(List list) {
        StringBuilder buffer = new StringBuilder();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object object = it.next();
            buffer.append(object.toString());
            if (it.hasNext()) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }
}
