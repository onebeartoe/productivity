
package org.onebeartoe.filesystem.find;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import org.onebeartoe.application.AppletService;

/**
 * @author Roberto Marquez
 */
public class FindetteService extends AppletService
{
    public void serviceRequest(FindetteRunProfile runProfile) throws IOException
    {
        switch (runProfile.mode) 
        {
            case SHOW_FILENAMES_WITH_SPECIAL_CHARACTERS:
            {
                showFilenamesWithSpecialCharacters(runProfile);
                
                break;
            }   
            default:
            {
                throw new AssertionError();
            }
        }
    }
    
    private void showFilenamesWithSpecialCharacters(FindetteRunProfile runProfile) throws IOException
    {
        String inpath = runProfile.inpath;
        Path dir = Paths.get(inpath);
        System.out.println("here are all the files:");
        int maxDepth = Integer.MAX_VALUE;
        
          String negatedAcceptedChars = "^([A-Za-z]|[0-9]|-|\\.|_)+$";
//        String negatedAcceptedChars = "^([A-Za-z]|[0-9]|-)+$";
        
        
        BiPredicate<Path, BasicFileAttributes> matcher = (p, bfa) -> 
//                bfa.isRegularFile()
//                && 
                ! p.getFileName().toString().matches(negatedAcceptedChars);
        
        Files.find(dir, maxDepth, matcher)
             .forEach(System.out::println);
    }
}
