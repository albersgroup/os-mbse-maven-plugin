package aero.albers.osmbse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Processes the sources (a single mdzip) as a mojo
 * 
 */
@Mojo(name = "mdzip-process-sources", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class MdzipProcessSourcesMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.groupId}", readonly = true)
    protected String groupId;

    @Parameter(defaultValue = "${project.artifactId}", readonly = true)
    protected String artifactId;

    @Parameter(defaultValue = "${project.version}", readonly = true)
    protected String version;

    @Parameter(defaultValue = "${project.build.directory}", readonly = true)
    protected File buildDirectory;

    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Processing resources mdzip project: ");
        Path baseDir = project.getBasedir().toPath();

        getLog().info("Looking for mdzip file");
        File origFile;
        try {
            origFile = Files.walk(baseDir)
                    .filter(path -> !path.toFile().isDirectory())
                    .filter(path -> !path.startsWith(baseDir.resolve("target"))) // Exclude the target directory
                    .filter(path -> path.getFileName().toString().endsWith(".mdzip"))
                    .map(Path::toFile)
                    .findFirst()
                    .orElseThrow(() -> new MojoFailureException("Could not locate .mdzip file"));
        } catch (IOException e) {
            throw new MojoFailureException("IOException occured while walking directory", e);
        }

        getLog().info("Copying resources to target directory");
        Path targetDir = baseDir.resolve("target");
        Path outputFile;
        try {
            targetDir.toFile().mkdir();
            String fname = origFile.getName();
            int extension = fname.lastIndexOf('.');
            String outFileName = fname.substring(0, extension) + "-" + this.version + fname.substring(extension);

            outputFile = targetDir.resolve(outFileName);

            Files.copy(origFile.toPath(), outputFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new MojoFailureException("Could not copy file to target directory");
        }

        getLog().info("Setting artifact file");

        // this tells maven what the file is that we want to install/deploy
        project.getArtifact().setFile(outputFile.toFile());
    }
}
