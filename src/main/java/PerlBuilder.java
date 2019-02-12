import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * Perl run-time utility.
 */
public class PerlBuilder
{
    /**
     * Command-line API.
     */
    private ProcessBuilder processBuilder;
    private BufferedReader bufferedReader;

    /**
     * Class members.
     */
    private File directory;
    private String command;
    private String output;

    /**
     * Constants.
     */
    private final String BLANK = "";
    private static final int EXIT_SUCCESS = 0;

    /**
     * Public Constructor (Builder Pattern).
     */
    public PerlBuilder()
    {
        processBuilder = new ProcessBuilder();
        return;
    }

    /**
     * Execute a perl command.
     *
     * ```
     * // Example checking Perl version
     * PerlBuilder perlBuilder = new PerlBuilder();
     * perlBuilder.executeCommand("-v");
     * system.out.println(perlBuilder.getOutput());
     *
     * ```
     *
     * @param command the command to execute
     * @return the instance
     */
    public PerlBuilder executeCommand(String command)
    {
        System.out.println("DEBUG:" + "Execute : 'perl " + command + "'");
        this.command = command;
        processBuilder.command("perl", command);
        executeProcess();
        return this;
    }

    /**
     * Execute a generic list of commands.
     *
     * @param commandList the command to execute
     * @return the instance
     */
    public PerlBuilder executeCommand(List<String> commandList)
    {
        processBuilder.command(commandList);
        executeProcess();
        return this;
    }

    public PerlBuilder setDirectory(String directory)
    {
        System.out.println("DEBUG:" + "setting directory to : " + directory);
        this.directory = new File(directory);
        if (!this.directory.exists())
        {
            System.out.println("WARN:" + "Directory does not exist : " + directory);
        }
        processBuilder.directory(this.directory);
        return this;
    }

    public PerlBuilder setEnvironmentVariable(String env, String var)
    {
        Map<String, String> environmentToVariable = processBuilder.environment();
        environmentToVariable.put(env, var);
        return this;
    }

    private void executeProcess()
    {
        try {
            if (this.directory != null)
            {
                System.out.println("DEBUG:" + "   From : " + this.directory.getAbsolutePath());
            }
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == EXIT_SUCCESS)
            {
                System.out.println("DEBUG:" + "Execution Success");
                this.output = null;
                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            }
            else
            {
                System.out.println("DEBUG:" + "Execution Failure");
                System.out.println("ERROR:" + "Unable to execute command : 'perl " + this.command + "'");
                bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                System.out.println("ERROR:" + "Output message :\n```\n" + getOutput() + "\n```");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getters/Setters.
     */
    public String getOutput()
    {
        if (bufferedReader == null) { return BLANK; }
        if (this.output != null) { return this.output; }

        StringBuffer output = new StringBuffer();
        try
        {
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                output.append(line + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        this.output = output.toString();
        return (this.output != null) ? this.output : BLANK;
    }

    public String getCommand()
    {
        return command;
    }
}
