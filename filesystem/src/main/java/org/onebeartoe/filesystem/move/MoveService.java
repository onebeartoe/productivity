
package org.onebeartoe.filesystem.move;

import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author Roberto Marquez
 */
public class MoveService
{
    private int renameFiles(MovetteRunProfile runProfile)
    {
        char invalid = runProfile.specialCharTarget;
        
        int count = 0;
        if( Character.isDigit(invalid) || Character.isLetter(invalid) || invalid == '.' || invalid == '-') 
        {
            String message = "This program will only remove non-alphanumeric chars; nor will it remove the '.' or '-' chars";
            String title = "No files were renamed";
            
            System.err.println(title + " - " + message);
            
            throw new IllegalArgumentException(message);
        }
        else 
        {
            char inval_char = runProfile.specialCharTarget;
            File sourceDir = new File(runProfile.inpath);

            if(!sourceDir.exists()) 
            {
                    return count;
            }

            File[] children = sourceDir.listFiles();

            for(File file : children) 
            {
                String name = file.getName();
                StringBuilder buff = new StringBuilder(name);
                int i = buff.indexOf(inval_char + "");

                if(i != -1) 
                {
                    while(i != -1) 
                    {
                        buff.deleteCharAt(i);
                        i = buff.indexOf(inval_char + "");				
                    }
                    name = buff.toString();

                    boolean actuallyPerformMove = false;
                    
                    if(actuallyPerformMove)
                    {
                        File new_file = new File(file.getParent(), name);
                        file.renameTo(new_file);                        
                    }

                    count++;
                }
            }
        }
        System.out.println(count + " files renamed.");
        System.out.println("Done.");

        return count;
    }
    
    public void serviceRequest(MovetteRunProfile runProfile)
    {
        switch (runProfile.mode)
        {
            case RENAME_SPECIAL_CHARACTER_FILENAMES:
            {
                renameFiles(runProfile);

                break;
            }
            default:
            {
                throw new AssertionError();
            }
        }
    }
}
