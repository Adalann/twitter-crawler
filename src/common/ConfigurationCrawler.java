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

    public ConfigurationCrawler()
    {
        this.PORT = -1;
        this.CLIENT_LIMIT = -1;
        this.FILTER = "";
        this.TWEET_LIMIT = -1;
        this.SAVEFILE_NAME = "";
    }

    @Override
    public String toString()
    {
        return "\tPort : " + PORT + "\n\tClient limit : " + CLIENT_LIMIT + "\n\tFilter : " + FILTER + "\n\tTweet limit : " + TWEET_LIMIT + "\n\tFileName : " + SAVEFILE_NAME;
    }
}
