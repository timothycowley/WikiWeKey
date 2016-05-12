package wekey.search;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class SearchTest {
    Search searcher = new Search();

    @Test
    public void patternTest1() {
        Assert.assertTrue("findifin this sentence".matches(".*?if.*?"));
    }

    @Test
    public void patternTest2() {
        Assert.assertTrue("find ifin this sentence".matches("(?i:.*?If.*?)"));
    }

    @Test
    public void patternTest3() {
        Assert.assertTrue("findif in this sentence".matches(".*?if.*?"));
    }

    @Test
    public void searchWithWordTest() {
        List<File> files = searcher.getMatchingFiles("test");
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }

    @Test
    public void searchWithCapitalLetterTest() throws IOException {
        List<File> files = searcher.getMatchingFiles("S ");
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }

    @Test
    public void searchWithEmptyStringTest() {
        List<File> files = searcher.getMatchingFiles("");
        System.out.println(files.size());
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }

    @Test
    public void trimStringTest() throws Exception {
        List<File> files = searcher.getMatchingFiles("   ");
        System.out.println(files.size());
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }
    
    @Test
    public void readFileDirectPathTest() throws Exception{
        List<File> files = searcher.getMatchingFiles("C:\\Users\\snehanay\\Documents\\CS5500\\WikiWeKey\\WikiWeKeyProjectMavenV2\\WikiNotes\\example");
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }
}
