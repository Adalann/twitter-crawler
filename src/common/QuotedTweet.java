/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les informations des Tweets traités
**/

package common;

import java.io.Serializable;

public class QuotedTweet implements Serializable
{
    private static final long serialVersionUID = 43333l;
    public final String id_str;
    public final String text;
    public final String created_at;
    public final int retweet_count;
    public final int quote_count;

    public QuotedTweet()
    {
        this.id_str = "";
        this.text = "";
        this.created_at = "";
        this.retweet_count = 0;
        this.quote_count = 0;
    }
}
