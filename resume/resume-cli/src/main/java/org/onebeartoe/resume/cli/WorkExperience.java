
package org.onebeartoe.resume.cli;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class WorkExperience 
{
    public String comanyName;
    
    public String location;
    
    public String title;
    
    public String startEndDate;
    
    public List<String> qualifications;
    
    public WorkExperience()
    {
        this.qualifications = new ArrayList();
    }
}
