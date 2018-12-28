
package org.onebeartoe.filesystem.move;

import org.onebeartoe.application.RunProfile;

/**
 * @author Roberto Marquez
 */
public class MovetteRunProfile extends RunProfile
{
    public RunMode mode;
    
    public String inpath;

    public char specialCharTarget;
    
    public String replacementTarget;
    
    public String replacmentString;
            
    public enum RunMode
    {
        JUST_PRINT_RENAME_COMMANDS,
        JUST_PRINT_SUBVERSION_RENAME_COMMANDS,
        RENAME_SPECIAL_CHARACTER_FILENAMES,
    }
}