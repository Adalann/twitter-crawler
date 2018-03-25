/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les informations des Tweets traités
**/

package common;

public class Tweet
{
    public final String id_str;
    public final String text;
    public final String created_at;
    public final Tweet retweeted_status;
    public final int retweet_count;
    public final Tweet quoted_status;
    public final int quote_count;

    public Tweet()
    {

    }


    public String toString()
    {
        return "\n ID du Tweet: "+id_str+"text: "+text+" \n \n RT:"+retweeted_status.toString();
    }

}
