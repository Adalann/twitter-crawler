/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les informations des Tweets traités
**/

package common;

public class Retweet implements Serializable
{
    private static final long serialVersionUID = 41111l;
    public final String id_str;
    public final String text;
    public final String created_at;
    public final int retweet_count;
    public final int quote_count;

    public Retweet()
    {
        this.id_str = "";
        this.text = "";
        this.created_at = "";
        this.retweet_count = 0;
        this.quote_count = 0;
    }

    public String toString()
    {
        return "\n ID du Tweet: "+id_str+"text: "+text+" \n \n RT:"+retweeted_status.toString();
    }

}
