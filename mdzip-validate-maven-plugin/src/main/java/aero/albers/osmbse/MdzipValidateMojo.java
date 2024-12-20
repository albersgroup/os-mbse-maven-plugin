package aero.albers.osmbse;

import java.io.File;
import java.nio.file.Path;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "mdzip-validate", defaultPhase = LifecyclePhase.VALIDATE)
public class MdzipValidateMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project.groupId}", readonly = true)
	private String groupId;

	@Parameter(defaultValue = "${project.artifactId}", readonly = true)
	private String artifactId;

	@Parameter(defaultValue = "${project.version}", readonly = true)
	private String version;

	@Parameter(defaultValue = "${project.build.directory}", readonly = true)
	private File buildDirectory;

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Validating mdzip project: ");

		// TODO verify that there's only one cameo file in the project dir
		Path baseDir = project.getBasedir().toPath();

		//TODO verify that the format of the POM file matches the expected dependencies in the actual mdzip file
	}
}