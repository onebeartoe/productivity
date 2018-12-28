
package org.onebeartoe.filesystem.find;

import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.onebeartoe.application.AppletService;
import org.onebeartoe.application.CommandLineInterfaceApplet;
import org.onebeartoe.application.RunProfile;

/**
 * @author Roberto Marquez
 */
public class Findette extends CommandLineInterfaceApplet
{
    private static final String SPECIAL_CHARACTER_FILENAMES = "specialCharacterFilenames";
    
    @Override
    public Options buildOptions()
    {
        Option outfile = Option.builder()
                        .required(false)
                        .longOpt(SPECIAL_CHARACTER_FILENAMES)
                        .hasArg(true)
                        .build();

        Options options = new Options();
        options.addOption(outfile);
        
        return options;
    }
    
    @Override
    protected AppletService getService() 
    {
        return new FindetteService();
    }    
    
    public static void main(String [] args) throws Exception
    {
        CommandLineInterfaceApplet app = new Findette();
        app.execute(args);
    }

    @Override
    public RunProfile parseRunProfile(final String[] args, Options options) throws ParseException
    {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        
        FindetteRunProfile runProfile = new FindetteRunProfile();
        
        if( cmd.hasOption(SPECIAL_CHARACTER_FILENAMES) )
        {
            runProfile.mode = FindetteRunProfile.RunMode.SHOW_FILENAMES_WITH_SPECIAL_CHARACTERS;
        }
        
        if(runProfile.mode == null)
        {
            runProfile.mode = FindetteRunProfile.RunMode.SHOW_FILENAMES_WITH_SPECIAL_CHARACTERS;
        }
        
        List<String> remainingArgs = cmd.getArgList();
        
        System.out.println("Remaining args:");
        remainingArgs.forEach(a -> System.out.println(a));

        if( remainingArgs.isEmpty() )
        {
            // by default, use the current directory as the path if no 
            // argument is given
            runProfile.inpath = ".";
        }
        else
        {
            // use the first argument as the path to the .scad files
            runProfile.inpath = remainingArgs.get(0);
        }
        
        return runProfile;
    }
}
