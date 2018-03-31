/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les informations des Tweets traités
**/

package common;

import java.io.Serializable;

public class Tweet implements Serializable
{
    private static final long serialVersionUID = 40000l;
    public final String id_str;
    public final User user;
    public final String text;
    public final String created_at;
    public final Retweet retweeted_status;
    public final int retweet_count;
    public final QuotedTweet quoted_status;
    public final int quote_count;

    public Tweet()
    {
        this.id_str = "";
        this.user = null;
        this.text = "";
        this.created_at = "";
        this.retweeted_status = null;
        this.retweet_count = 0;
        this.quoted_status = null;
        this.quote_count = 0;
    }

    @Override
    public String toString()
    {
        return "{id=" + id_str + ", user=" + user + ", text=" + text + ", created_at=" + created_at + ", retweeted_status=" + retweeted_status + ", rt=" + retweet_count + ", quoted_status=" + quoted_status + ", quoted_count=" + quote_count + "}";
    }
}
