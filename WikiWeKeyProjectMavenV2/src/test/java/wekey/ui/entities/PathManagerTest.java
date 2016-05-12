package wekey.ui.entities;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

public class PathManagerTest {

    PathManager tester = new PathManager();

    @Test
    public void wikiNotesTest() {
        Assert.assertTrue(Files.exists(Paths.get("WikiNotes")));
    }

    @Test
    public void LogsTest() {
        Assert.assertTrue(Files.exists(Paths.get("application/logs")));
    }
}
