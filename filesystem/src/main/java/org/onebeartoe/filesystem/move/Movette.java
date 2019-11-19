
package org.onebeartoe.filesystem.move;

import static java.lang.System.out;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.onebeartoe.application.duration.DurationService;
import org.onebeartoe.application.logging.SysoutLoggerFactory;

/**
 * This application renames regular files to remove special characters.
 * 
 * @author Roberto Marquez
 */
public class Movette
{
    private static final String INPUT_DIRECTORY = "inputDirectory";
    
    private static final String JUST_PRINT_RENAME_COMMANDS = "justPrintRenameCommands";
    
    private static final String REMOVE_SPECIAL_CHARACTER_FILENAMES = "removeSpecialCharacterFilenames";

    private Options buildOptions()
    {
        Option inputDirectory = Option.builder()
                                      .required()
                                      .hasArg()
                                      .longOpt(INPUT_DIRECTORY)
                                      .build();
                
        Option justPrintRenameCommands = Option.builder()
                                            .longOpt(JUST_PRINT_RENAME_COMMANDS)
                                            .desc("Instead of actually moving the files, this only prints the mv commands to rename the files.")
                                            .build();
        
        Option removeSpecialCharacterFilenames = Option.builder()
                                                    .required()
                                                    .longOpt(REMOVE_SPECIAL_CHARACTER_FILENAMES)
                                                    .hasArg(true)
                                                    .desc("Specifiying this paramter put the application in replace character mode and a" 
                                                            + "The argument is the String target to be replaced.")
                                                    .build();
        
        Options options = new Options();
        options.addOption(inputDirectory);
        options.addOption(justPrintRenameCommands);
        options.addOption(removeSpecialCharacterFilenames);
        
        return options;
    }
    
    public static void main(String [] args)
    {
        Logger logger = SysoutLoggerFactory.getLogger( Movette.class.getName() );
        
        Movette movette = new Movette();
        Options options = movette.buildOptions();
        
        try
        {
            MovetteRunProfile runProfile = movette.parseRunProfile(args, options);

            Instant start = Instant.now();

            MoveService movetteService = new MoveService();
            movetteService.serviceRequest(runProfile);

            Instant end = Instant.now();
        
            DurationService durationService = new DurationService();
            String message = "Movette ran for " + durationService.durationMessage(start, end) + "\n";
            logger.info(message);
        }
        catch(ParseException uoe)
        {
            uoe.printStackTrace();
            
            String usage = "\n" + "java -jar findette.jar " + "--" + movette.INPUT_DIRECTORY + " <input-directory>" + 
                           " --" + movette.REMOVE_SPECIAL_CHARACTER_FILENAMES + " <special-char>" + "\n";
            
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(usage, options);
        } 
    }

    private MovetteRunProfile parseRunProfile(final String[] args, Options options) throws ParseException
    {
        CommandLineParser parser = new DefaultParser();
        CommandLine cl = parser.parse(options, args);
        
        MovetteRunProfile runProfile = new MovetteRunProfile();

        runProfile.inpath = cl.getOptionValue(INPUT_DIRECTORY);

        runProfile.mode = MovetteRunProfile.RunMode.RENAME_SPECIAL_CHARACTER_FILENAMES;

        String s = cl.getOptionValue(REMOVE_SPECIAL_CHARACTER_FILENAMES);
        if(s.length() != 1)
        {
            String message = "The value of the " + REMOVE_SPECIAL_CHARACTER_FILENAMES + " shold be a single character.";

            throw new ParseException(message);
        }
        runProfile.specialCharTarget = s.charAt(0);
     
        if( cl.hasOption(JUST_PRINT_RENAME_COMMANDS) )
        {
            runProfile.mode = MovetteRunProfile.RunMode.JUST_PRINT_RENAME_COMMANDS;
        }
        
        List<String> remainingArgs = cl.getArgList();
        
        if( !remainingArgs.isEmpty() )
        {
            System.out.println("Remaining args:");
            
            remainingArgs.forEach(System.out::println);
        }
        
        return runProfile;
    }    
}
