
package org.onebeartoe.filesystem.find;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.onebeartoe.application.AppletService;
import org.onebeartoe.application.RunProfile;

/**
 * @author Roberto Marquez
 */
public class FindetteService extends AppletService
{
    @Override
    public void serviceRequest(RunProfile runProfile) throws IOException
    {
        FindetteRunProfile frp = (FindetteRunProfile) runProfile;
        
        switch (frp.mode) 
        {
            case SHOW_FILENAMES_WITH_SPECIAL_CHARACTERS:
            {
                showFilenamesWithSpecialCharacters(frp);
                
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
        
        BiPredicate<Path, BasicFileAttributes> matcher = (p, bfa) ->
                ! p.getFileName().toString().matches(negatedAcceptedChars);
        
        try( Stream<Path> find = Files.find(dir, maxDepth, matcher) )
        {    
            find.forEach(System.out::println);
        } 
    }
}
