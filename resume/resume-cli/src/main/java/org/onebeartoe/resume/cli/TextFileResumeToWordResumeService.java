
package org.onebeartoe.resume.cli;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import org.onebeartoe.application.AppletService;
import org.onebeartoe.application.RunProfile;

/**
 *
 */
class TextFileResumeToWordResumeService extends AppletService
{
    @Override
    public void serviceRequest(RunProfile runProfile) throws Exception
    {
        //TODO: make the delcartion use generics to avoid the cast!!!!!!        
        TextFileResumeToWordResumeProfile p = (TextFileResumeToWordResumeProfile)
                runProfile;
        
        File infile = p.infile;
        
        List<String> allLines = Files.readAllLines( infile.toPath() );
        
        SectionModes sectionMode = null;
        
        Resume resume = new Resume();
        
        for(String line : allLines)
        {                        
            if(lineIsASectionHeader(line) )
            {
                sectionMode = SectionModes.valueOf(line);
            }
            else
            {
                processSectionItem(resume, sectionMode, line);
            }
        }
    }
    
    private boolean lineIsASectionHeader(String line)
    {
        String str = line.trim();
        
        List<String> headers = List.of("OBT_RESUME_SUMMARY",
                                        "OBT_RESUME_EXERIENCE_ITEM",
                                        "OBT_RESUME_EDUCATION_ITEM");
        
        boolean isHeader = headers.contains(str);
        
        return isHeader;
    }
    
    private void processSectionItem(Resume resume, SectionModes mode, String line)
    {
        switch(mode)
        {
            case OBT_RESUME_SUMMARY:
            {
                resume.addSummaryItem(line);
                
                break;
            }
            default:
            {
                throw new IllegalArgumentException("An unknown mode was encountered.");
            }
        }
    }
}
