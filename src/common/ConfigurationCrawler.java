/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les constantes du programme.
*/

package common;

import java.io.*;

public class ConfigurationCrawler extends Configuration
{
    public final int PORT;
    public final int CLIENT_LIMIT;
    public final String FILTER;
    public final int TWEET_LIMIT;
    public final String SAVEFILE_NAME;
    public final String RESTOREFILE_NAME;
    public final boolean RESTORE_ON_START;

    public ConfigurationCrawler()
    {
        this.PORT = 2200;
        this.CLIENT_LIMIT = -1;
        this.FILTER = "#twitter";
        this.TWEET_LIMIT = -1;
        this.SAVEFILE_NAME = "output.data";
        this.RESTOREFILE_NAME = "restore.data";
        this.RESTORE_ON_START = false;
        if(ERROR_STREAM == null)
        {
            try
            {
                File f = new File("../error_log_crawler.txt");
                int i = 1;
                while(f.exists())
                {
                    f = new File("../error_log_crawler_" + i++ + ".txt");
                }
                System.out.println("Error filename : " + f.getName());
                ERROR_STREAM = new PrintStream(f);
            }
            catch(IOException e)
            {
                e.printStackTrace();
                System.out.println(ANSI_RED + "Error, error_stream redirected to the console." + ANSI_RESET);
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
        return "\tPort : " + PORT + "\n\tClient limit : " + CLIENT_LIMIT + "\n\tFilter : " + FILTER + "\n\tTweet limit : " + TWEET_LIMIT + "\n\tSaveFile Name : " + SAVEFILE_NAME + "\n\tRestoreFile Name : " + RESTOREFILE_NAME + "\n\tRestore on start : " + RESTORE_ON_START;
    }
}
