
package org.onebeartoe.filesystem.find;

import org.onebeartoe.application.RunProfile;

/**
 * @author Roberto Marquez
 */
public class FindetteRunProfile extends RunProfile
{
    public RunMode mode;
    
    public String inpath;
            
    public enum RunMode
    {
        SHOW_FILENAMES_WITH_SPECIAL_CHARACTERS,
    }
}
