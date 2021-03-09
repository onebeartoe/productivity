
package org.onebeartoe.resume.cli;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
class Schooling 
{
    public String schoolName;
    
    public String location;
    
    public String major;
    
    public String startEndDate;
    
    public List<String> summaries;
    
    public Schooling()
    {
        this.summaries = new ArrayList();
    }    
}
