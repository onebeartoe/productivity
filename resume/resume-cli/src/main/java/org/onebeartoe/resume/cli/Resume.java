
package org.onebeartoe.resume.cli;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Resume
{
    public String candidateName;
    
    public String candidateLocation;
    
    public String candidateEmail;

    public String candidatSocials;
    
    public List<String> summary;

    public List<WorkExperience> workExperiences;
    
    public Resume()
    {
        summary = new ArrayList();
        
        workExperiences = new ArrayList();
    }       
    
    public void addSummaryItem(String line) 
    {
        summary.add(line);
    }
    
//    public void addWorkExperienceItem(String item)
//    {
//        k
//    }
}
