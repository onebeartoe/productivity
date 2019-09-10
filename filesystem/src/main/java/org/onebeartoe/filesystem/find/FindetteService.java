
package org.onebeartoe.filesystem.find;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
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
        
        Stream<Path> paths = null;
        
        System.out.println("here are all the files:");
        
        switch (frp.mode) 
        {
            case SHOW_FILENAMES_WITH_SPECIAL_CHARACTERS:
            {
                paths = showFilenamesWithSpecialCharacters(frp);
                
                paths.forEach(System.out::println);
                
                break;
            }   
            default:
            {
                throw new AssertionError();
            }
        }
    }
    
    public Stream<Path> showFilenamesWithSpecialCharacters(FindetteRunProfile runProfile) throws IOException
    {
        String inpath = runProfile.inpath;
        Path dir = Paths.get(inpath);

        int maxDepth = Integer.MAX_VALUE;
        
        String negatedAcceptedChars = "^([A-Za-z]|[0-9]|-|\\.|_)+$";
        
        BiPredicate<Path, BasicFileAttributes> matcher = (p, bfa) ->
                ! p.getFileName().toString().matches(negatedAcceptedChars);
        
        Stream<Path> paths = Files.find(dir, maxDepth, matcher);
                
        return paths;
    }
}
