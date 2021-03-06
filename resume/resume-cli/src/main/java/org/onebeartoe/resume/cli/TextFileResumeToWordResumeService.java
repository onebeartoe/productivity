
package org.onebeartoe.resume.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.onebeartoe.application.AppletService;
import org.onebeartoe.application.Dates;
import org.onebeartoe.application.RunProfile;
import org.onebeartoe.poi.ParagraphProfile;
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
            throw new IllegalStateException("The resume infile has no content.");
        }
        
        SectionModes sectionMode = null;
        
        Resume resume = new Resume();
        
        for(String line : allLines)
        {                        
            if( lineIsASectionHeader(line) )
            {
                System.out.println("line is header: " + line);
                
                sectionMode = SectionModes.valueOf(line);
                
                if(sectionMode == SectionModes.OBT_RESUME_EXERIENCE)
                {            
                    WorkExperience experience = new WorkExperience();

                    resume.workExperiences.add(experience);
                }
                else if(sectionMode == SectionModes.OBT_RESUME_EDUCATION)
                {
                    Schooling schooling = new Schooling();
                    
                    resume.educations.add(schooling);
                }
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

    private void candidateToDocx(Resume resume, XWPFDocument document) 
    {
        XWPFParagraph nameParagraph = document.createParagraph();        
        boolean bold = true;        
        String text = resume.candidateName;        
        poiDocuments.smallCapsCase(nameParagraph, text, bold);        
        nameParagraph.setAlignment(ParagraphAlignment.CENTER);
        
        XWPFParagraph locationParagraph = document.createParagraph();
        locationParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun locationRun = locationParagraph.createRun();
        locationRun.setText(resume.candidateLocation);
        locationRun.addBreak();
                
        poiDocuments.addBreak(document);
        
        XWPFParagraph contactParagraph = document.createParagraph();
        contactParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun contactRun = locationParagraph.createRun();
        contactRun.setText(resume.candidateEmail + " " + resume.candidatSocials);
    }

    private void experienceToDocx(Resume resume, XWPFDocument document) 
    {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun titleRun = paragraph.createRun();
//titleRun.addBreak();
        titleRun.setSmallCaps(true);
        titleRun.setText("Experience");       
        paragraph.setBorderBottom(Borders.SINGLE);
                
        System.out.println("experience count = " + resume.workExperiences.size() );
        
        for(WorkExperience experience : resume.workExperiences)
        {
            // company name and location row
            ParagraphProfile compnayNameTextProile = new ParagraphProfile();
            compnayNameTextProile.bold = true;
            compnayNameTextProile.smallCaps = true;
            compnayNameTextProile.text = experience.comanyName;

            ParagraphProfile locationTextProfile = new ParagraphProfile();
            locationTextProfile.text = experience.location;
            locationTextProfile.smallCaps = true;
            poiDocuments.edgeAlignedText(document, compnayNameTextProile, locationTextProfile);

            // title and start/end date row
            ParagraphProfile titleProile = new ParagraphProfile();
            titleProile.smallCaps = true;
            titleProile.text = experience.title;            
            ParagraphProfile startEndProfile = new ParagraphProfile();
            startEndProfile.smallCaps = true;
            startEndProfile.text = experience.startEndDate;
            poiDocuments.edgeAlignedText(document, titleProile, startEndProfile);

            poiDocuments.unorderedList(document, experience.qualifications);
        }
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
//            System.out.println("could not process line>" + line + "< e = " + e);
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
            case OBT_RESUME_EXERIENCE:
            {
                processOneWorkExperienceSectionLine(resume, line);
                
                break;
            }
            case OBT_RESUME_EDUCATION:
            {
                processOneEducationSectionLine(resume, line);
                
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
        
        candidateToDocx(resume, document);
    
        boolean showSummary = false;
        
        if(showSummary)
        {
            summaryToDocx(resume, document);
        }
  
        experienceToDocx(resume, document);
        
        educationToDocx(resume, document);
        
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
            else if( resume.candidateLocation == null)
            {
                resume.candidateLocation = trimmedLine;
            }
            else if( resume.candidateEmail == null)
            {
                resume.candidateEmail = trimmedLine;
            }
            else if( resume.candidatSocials == null)
            {
                resume.candidatSocials = trimmedLine;
            }
        }
    }

    private void summaryToDocx(Resume resume, XWPFDocument document) 
    {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun titleRun = paragraph.createRun();
        titleRun.setSmallCaps(true);
        titleRun.setText("Summary");       
        paragraph.setBorderBottom(Borders.SINGLE);

        for(String summary : resume.summary)
        {
            XWPFParagraph summaryParagraph = document.createParagraph();
            XWPFRun summaryRun = summaryParagraph.createRun();
            summaryRun.setText(summary);
        }
    }

    private void processOneWorkExperienceSectionLine(Resume resume, String line) 
    {
        if( line == null || line.trim().isBlank() )
        {
            // no operation
        }
        else
        {            
            int i = resume.workExperiences.size() - 1;
            
            WorkExperience currentExperience = resume.workExperiences.get(i);                        
        
            String trimmedLine = line.trim();
            
            if( currentExperience.comanyName == null)
            {
                currentExperience.comanyName = trimmedLine;
            }
            else if( currentExperience.location == null )
            {
                currentExperience.location = trimmedLine;
            }
            else if (currentExperience.title == null)
            {
                currentExperience.title = trimmedLine;
            }
            else if( currentExperience.startEndDate == null)
            {
                currentExperience.startEndDate = trimmedLine;
            }
            else
            {
                // assume it is a qualification
                currentExperience.qualifications.add(trimmedLine);
            }
        }
    }

    private void processOneEducationSectionLine(Resume resume, String line) 
    {
        if( line == null || line.trim().isBlank() )
        {
            // no operation
        }
        else
        {
            int i = resume.educations.size() - 1;

            Schooling currentExperience = resume.educations.get(i);                        

            String trimmedLine = line.trim();

            if( currentExperience.schoolName == null)
            {
                currentExperience.schoolName = trimmedLine;
            }
            else if( currentExperience.location == null )
            {
                currentExperience.location = trimmedLine;
            }
            else if (currentExperience.major == null)
            {
                currentExperience.major = trimmedLine;
            }
            else if( currentExperience.startEndDate == null)
            {
                currentExperience.startEndDate = trimmedLine;
            }
            else
            {
                // assume it is a summary
                currentExperience.summaries.add(trimmedLine);
            }
        }
    }

    private void educationToDocx(Resume resume, XWPFDocument document) 
    {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun titleRun = paragraph.createRun();
//titleRun.addBreak();
        titleRun.setSmallCaps(true);
        titleRun.setText("Education");       
        paragraph.setBorderBottom(Borders.SINGLE);
                
        System.out.println("schooling count = " + resume.educations.size() );
        
        for(Schooling schooling : resume.educations)
        {
            // company name and location row
            ParagraphProfile compnayNameTextProile = new ParagraphProfile();
            compnayNameTextProile.bold = true;
            compnayNameTextProile.smallCaps = true;
            compnayNameTextProile.text = schooling.schoolName;

            ParagraphProfile locationTextProfile = new ParagraphProfile();
            locationTextProfile.text = schooling.location;
            locationTextProfile.smallCaps = true;
            poiDocuments.edgeAlignedText(document, compnayNameTextProile, locationTextProfile);

            // title and start/end date row
            ParagraphProfile titleProile = new ParagraphProfile();
            titleProile.smallCaps = true;
            titleProile.text = schooling.major;  
            ParagraphProfile startEndProfile = new ParagraphProfile();
            startEndProfile.smallCaps = true;
            startEndProfile.text = schooling.startEndDate;
            poiDocuments.edgeAlignedText(document, titleProile, startEndProfile);

            poiDocuments.unorderedList(document, schooling.summaries);
        }
    }
}