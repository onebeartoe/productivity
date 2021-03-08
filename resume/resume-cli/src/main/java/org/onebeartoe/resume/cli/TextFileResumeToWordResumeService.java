
package org.onebeartoe.resume.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.onebeartoe.application.AppletService;
import org.onebeartoe.application.Dates;
import org.onebeartoe.application.RunProfile;
import org.onebeartoe.poi.PoiDocuments;

/**
 *
 */
class TextFileResumeToWordResumeService extends AppletService
{
    private PoiDocuments poiDocuments = new PoiDocuments();
            
    @Override
    public void serviceRequest(RunProfile runProfile) throws Exception
    {
        //TODO: make the delcartion use generics to avoid the cast!!!!!!        
        TextFileResumeToWordResumeProfile p = (TextFileResumeToWordResumeProfile)
                runProfile;
        
        File infile = p.infile;
        
        List<String> allLines = Files.readAllLines( infile.toPath() );
        
        if(allLines.size() == 0)
        {
            throw new IllegalStateException("The resume infile has no content");
        }
else
{
    System.out.println("allLines = " + allLines);
    System.out.println("allLines.size = " + allLines.size() );
}            
        
        SectionModes sectionMode = null;
        
        Resume resume = new Resume();
        
        for(String line : allLines)
        {                        
            if( lineIsASectionHeader(line) )
            {
                System.out.println("line is header: " + line);
                
                sectionMode = SectionModes.valueOf(line);
            }
            else
            {
                if(sectionMode == null)
                {
                    System.out.println("line drop = " + line);
                    
                    continue;
                }
                else
                {
                    processSectionItem(resume, sectionMode, line);
                }
                
            }
        }
        
        String shortDate = Dates.shortFilesystemDate( new Date() );
        
        String outfileName = "target/" + infile.getName() + "." + shortDate + ".docx";
        
        File outfile = new File(outfileName);
        
        toDocx(outfile, resume);
    }
    
    private boolean lineIsASectionHeader(String line)
    {
        String str = line.trim();
        
        List<SectionModes> headers = List.of( SectionModes.values() );

        boolean isHeader = false;
        
        try
        {
            SectionModes target = SectionModes.valueOf(str);
            
            isHeader = headers.contains(target);
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("could not process line>" + line + "< e = " + e);
        }        
        
        return isHeader;
    }
    
    private void processSectionItem(Resume resume, SectionModes mode, String line)
    {
        switch(mode)
        {
            case OBT_RESUME_CANDIDATE:
            {
                processOneCandidateSection(resume, line);
                
                break;
            }
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

    private void toDocx(File outfile, Resume resume) throws FileNotFoundException, IOException 
    {
        System.out.println("outfile = " + outfile.toString() );
        
        PoiDocuments poiDocuments = new PoiDocuments();

        FileOutputStream outStream = new FileOutputStream(outfile);
        
        XWPFDocument document = resumeToDocx(resume);
        
        poiDocuments.writeDocument(outStream, document);
    }

    private XWPFDocument resumeToDocx(Resume resume)
    {
        XWPFDocument document = new XWPFDocument();
        
        XWPFParagraph paragraph = document.createParagraph();
        
        boolean bold = true;
        
        String text = resume.candidateName;
        
        poiDocuments.smallCapsCase(paragraph, text, bold);
        
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        
        return document;
    }

    private void processOneCandidateSection(Resume resume, String line) 
    {
        if( line == null || line.trim().isBlank() )
        {
            // no operation
        }
        else
        {
            String trimmedLine = line.trim();
            
            if( resume.candidateName == null)
            {
                resume.candidateName = trimmedLine;
            }
            else if( resume.candidateEmail == null)
            {
                resume.candidateEmail = trimmedLine;
            }
        }
    }
}
