
package org.onebeartoe.resume.cli;

import java.io.File;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.Test;

/**
 *
 */
public class TextFileResumeToWordResumeServiceSpecification
{
    private TextFileResumeToWordResumeService implementation = new TextFileResumeToWordResumeService();
    
    @Test
    public void serviceRequest() throws Exception
    {
        TextFileResumeToWordResumeProfile profile = new TextFileResumeToWordResumeProfile();
        
        profile.infile = new File("src/test/resources/roberto-marquez.resume");
        
        implementation.serviceRequest(profile);
        
        assertTrue( profile.infile.exists() );
        
        assertTrue( profile.infile.length() > 0 );
    }
}
