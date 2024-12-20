package aero.albers.osmbse;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests for main mojo
 * 
 */
public class MdzipProcessSourcesTest {
    private static final Logger logger = LoggerFactory.getLogger(MdzipProcessSourcesTest.class);

    /**
     * Empty simulated directory test
     * 
     * @throws IOException if cannot create temp dir
     */
    @Test
    public void testEmptyDirectory() throws IOException {
        MdzipProcessSourcesMojo mojo = new MdzipProcessSourcesMojo();

        File tempDir = Files.createTempDirectory("test-basedir").toFile();
        tempDir.deleteOnExit();
        File testPom = new File(tempDir, "test.pom");
        testPom.createNewFile();
        testPom.deleteOnExit();

        MavenProject project = new MavenProject();
        project.setFile(testPom);
        mojo.project = project;

        MojoFailureException ex = assertThrows(MojoFailureException.class, () -> {
            mojo.execute();
        });
    }

    /**
     * Single file not mdzip in the simulated directory
     * 
     * @throws IOException if cannot create temp dir
     */
    @Test
    public void testSingleNotMdzip() throws IOException {
        MdzipProcessSourcesMojo mojo = new MdzipProcessSourcesMojo();

        File tempDir = Files.createTempDirectory("test-basedir").toFile();
        tempDir.deleteOnExit();
        File testPom = new File(tempDir, "test.pom");
        testPom.createNewFile();
        testPom.deleteOnExit();

        File file = new File(tempDir, "test.txt");
        file.deleteOnExit();

        MavenProject project = new MavenProject();
        project.getArtifact();

        project.setFile(testPom);
        mojo.project = project;

        MojoFailureException ex = assertThrows(MojoFailureException.class, () -> {
            mojo.execute();
        });
    }

    /**
     * Full test with working mdzip including copy to target file
     * 
     * @throws IOException if cannot create temp dir
     */
    @Test
    public void testSingleMdzip() throws IOException {
        MdzipProcessSourcesMojo mojo = new MdzipProcessSourcesMojo();

        File tempDir = Files.createTempDirectory("test-basedir").toFile();
        tempDir.deleteOnExit();
        File testPom = new File(tempDir, "test.pom");
        testPom.createNewFile();
        testPom.deleteOnExit();

        File file = new File(tempDir, "test.mdzip");
        file.deleteOnExit();
        file.createNewFile();

        String version = "1.0.0";
        MavenProject project = new MavenProject();
        project.setFile(testPom);
        mojo.project = project;
        mojo.version = version;
        Artifact artifact = new DefaultArtifact("group", "artifact", version, "scope", "type", "classifier", null);
        project.setArtifact(artifact);

        try {
            mojo.execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed execution", e);
        }

        logger.info("Checking the target dir exists");
        Path target = tempDir.toPath().resolve("target");
        assertTrue(target.toFile().exists());

        String expectedFname = "test" + "-" + version + ".mdzip";
        Path outFile = target.resolve(expectedFname);
        assertTrue(outFile.toFile().exists());

        outFile.toFile().deleteOnExit();
    }

}
