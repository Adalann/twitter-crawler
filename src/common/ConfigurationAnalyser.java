/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les constantes du programme.
*/

package common;

import java.io.*;

public class ConfigurationAnalyser extends Configuration
{
    public final int PORT;
    public final int CLIENT_LIMIT;
    public final int HITS_REPETITION;
    public final String SAVEFILE_NAME;
    public final String RESTOREFILE_NAME;
    public final boolean RESTORE_ON_START;

    public ConfigurationAnalyser()
    {
        this.PORT = -1;
        this.CLIENT_LIMIT = -1;
        this.HITS_REPETITION = 10;
        this.SAVEFILE_NAME = "";
        this.RESTOREFILE_NAME = "";
        this.RESTORE_ON_START = false;
        if(ERROR_STREAM == null)
        {
            try
            {
                File f = new File("../error_log_analyser.txt");
                int i = 1;
                while(f.exists())
                {
                    f = new File("../error_log_analyser_" + i++ + ".txt");
                }
                System.out.println("Error filename : " + f.getName());
                ERROR_STREAM = new PrintStream(f);
            }
            catch(IOException e)
            {
                e.printStackTrace();
                System.out.println(ANSI_RED + "Error, error_stream redirected to console." + ANSI_RESET);
                ERROR_STREAM = System.err;
            }
        }
    }

    public PrintStream ERROR_STREAM()
    {
        return ERROR_STREAM;
    }

    @Override
    public String toString()
    {
        return "Port : " + PORT + "\n\tClient limit : " + CLIENT_LIMIT + "\n\tHITS repetition : " + HITS_REPETITION + "\n\tSaveFile Name : " + SAVEFILE_NAME + "\n\tRestoreFile Name : " + RESTOREFILE_NAME + "\n\tRestore on start : " + RESTORE_ON_START;
    }
}
