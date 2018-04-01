/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les constantes du programme.
*/

package common;

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
        this.PORT = -1;
        this.CLIENT_LIMIT = -1;
        this.FILTER = "";
        this.TWEET_LIMIT = -1;
        this.SAVEFILE_NAME = "";
        this.RESTOREFILE_NAME = "";
        this.RESTORE_ON_START = false;
    }

    @Override
    public String toString()
    {
        return "\tPort : " + PORT + "\n\tClient limit : " + CLIENT_LIMIT + "\n\tFilter : " + FILTER + "\n\tTweet limit : " + TWEET_LIMIT + "\n\tSaveFile Name : " + SAVEFILE_NAME + "\n\tRestoreFile Name : " + RESTOREFILE_NAME + "\n\tRestore on start : " + RESTORE_ON_START;
    }
}
