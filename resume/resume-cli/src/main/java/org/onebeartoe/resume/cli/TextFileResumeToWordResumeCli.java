 
package org.onebeartoe.resume.cli;

import java.io.File;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.onebeartoe.application.CommandLineInterfaceApplet;

/**
 *
 */
public class TextFileResumeToWordResumeCli extends CommandLineInterfaceApplet
{
    private TextFileResumeToWordResumeService service = new TextFileResumeToWordResumeService();
    
    final String IN_FILE = "inFile";
    
    @Override
    public Options buildOptions()
    {
        Option inFile = Option.builder()
                                .hasArg()
                                .optionalArg(true)
                                .longOpt(IN_FILE)
                                .build();
        
        Options options = new Options();
        
        options.addOption(inFile);
        
        return options;
    }
    
    @Override
    public TextFileResumeToWordResumeService getService()
    {
        return service;
    }
    
    public static void main(String [] args) throws Exception
    {
        CommandLineInterfaceApplet app = new TextFileResumeToWordResumeCli();        
        
        app.execute(args);        
    }
    
    @Override
    protected TextFileResumeToWordResumeProfile parseRunProfile(final String[] args, Options options) throws ParseException
    {
        CommandLineParser parser = new DefaultParser();
        
        CommandLine cli = parser.parse(options, args);

        TextFileResumeToWordResumeProfile profile = new TextFileResumeToWordResumeProfile();
        
        if( cli.hasOption(IN_FILE) )
        {
            if( cli.getOptions().length > 1 )
            {
                String message = "other options are not accepted with config file option";
                
                throw new ParseException(message);
            }
            
            String configPath = cli.getOptionValue(IN_FILE);
            
            File infile = new File(configPath);    
            
            profile.infile = infile;
        }
        else
        {
            List<String> argList = cli.getArgList();
            
            if(argList.size() < 1)
            {
                throw new ParseException("At least one command line argument is needed, the value of the infile.");
            }
            else
            {
                String str = argList.get(0);
                
                str = str.trim();
                
                profile.infile = new File(str);
            }
        }

        return profile;
    }
}
