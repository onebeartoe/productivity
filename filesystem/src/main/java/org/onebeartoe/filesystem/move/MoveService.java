
package org.onebeartoe.filesystem.move;

import java.io.File;
import org.onebeartoe.application.AppletService;
import org.onebeartoe.application.RunProfile;

/**
 * @author Roberto Marquez
 */
public class MoveService extends AppletService
{
    private int renameFiles(MovetteRunProfile runProfile)
    {
        int count = 0;

        char invalidChar = runProfile.specialCharTarget;
        File sourceDir = new File(runProfile.inpath);

        if(!sourceDir.exists()) 
        {
            throw new IllegalArgumentException("The input path does not exist: " + runProfile.inpath);
        }

        File[] children = sourceDir.listFiles();

        for(File file : children) 
        {
            String searchTarget = String.valueOf(invalidChar);

            if( file.getName().contains(searchTarget) ) 
            {
                String newName = file.getName().replaceAll(searchTarget, "");

                File newFile = new File(file.getParent(), newName);

                switch (runProfile.mode)
                {
                    case JUST_PRINT_RENAME_COMMANDS:
                    {
                        String moveCommand = "mv " + file.toString() + " " + newFile.toString();
                        System.out.println(moveCommand);                            

                        break;
                    }
                    case RENAME_SPECIAL_CHARACTER_FILENAMES:
                    {
                        System.out.println("Processing: " + newFile.toString() );
                                                
                        if( newFile.exists() )
                        {
                            String message = "A file already exists with the rename: " + newFile.getAbsolutePath();

                            throw new IllegalArgumentException(message);
                        }
                        else
                        {
                            boolean renamed = file.renameTo(newFile);
                            
                            if(renamed)
                            {
                                count++;
                            }
                        }

                        break;
                    }
                    default:
                    {
                        throw new AssertionError("The run mode is not set.");
                    }
                }
            }
        }

        return count;
    }
    
    @Override
    public void serviceRequest(RunProfile runProfile)
    {
        MovetteRunProfile mrp = (MovetteRunProfile) runProfile;
        
        char invalid = mrp.specialCharTarget;
        
        int count = 0;
        if( Character.isDigit(invalid) || Character.isLetter(invalid) || invalid == '.' || invalid == '-') 
        {
            String title = "No files were renamed - ";
            String message = title + "This program will only remove non-alphanumeric chars; nor will it remove the '.' or '-' chars";
            
            throw new IllegalArgumentException(message);
        }
        else 
        {
            renameFiles(mrp);
                    
            System.out.println(count + " files renamed.");
            System.out.println("Done.");
        }
    }
}
