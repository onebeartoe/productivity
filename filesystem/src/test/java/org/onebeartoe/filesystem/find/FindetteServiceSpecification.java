
package org.onebeartoe.filesystem.find;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import static org.onebeartoe.filesystem.find.FindetteRunProfile.RunMode.SHOW_FILENAMES_WITH_SPECIAL_CHARACTERS;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * These tests define the specification for FindetteService.
 */
public class FindetteServiceSpecification
{
    FindetteService implementation;
    
    @BeforeMethod
    public void setUpMethod() throws Exception
    {
        implementation = new FindetteService();
    }

    /**
     * Assert the method finds files with invalid characters 
     * in the filename, under the target/ directory.
     */
    @Test
    public void showFilenamesWithSpecialCharacters() throws Exception
    {
        // create the directory for test data
        String inpath = "target/test-data/findette/";
        File testDataDir = new File(inpath);
        Path testDataPath = testDataDir.toPath();
        Files.createDirectories(testDataPath);

        // create an invlaid 'star' directory
        File starFile = new File(testDataDir, "st*r.text");
        Path starPath = starFile.toPath();
        Files.createFile(starPath);
        
        FindetteRunProfile runProfile = new FindetteRunProfile();
        runProfile.mode = SHOW_FILENAMES_WITH_SPECIAL_CHARACTERS;
        runProfile.inpath = inpath;
        
        Stream<Path> paths = implementation.showFilenamesWithSpecialCharacters(runProfile);
        Object[] array = paths.toArray();
     
        int expected = 1;
        
        int actual = array.length;
        
        assertEquals(actual, expected);
    }    
}
