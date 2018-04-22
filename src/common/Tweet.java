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
    public final String id_str;                // l'id du tweet
    public final User user;                    // son auteur
    public final String text;                  // son texte
    public final Entities entities;            // les hashtags et urls
    public final String created_at;            // la date de creation
    public final Retweet retweeted_status;     // le tweet retweete
    public final int retweet_count;            // le nombre de rewteet
    public final QuotedTweet quoted_status;    // le tweet cite
    public final int quote_count;              // le nombre de citation

    public Tweet()
    {
        this.id_str = "";
        this.user = null;
        this.text = "";
        this.entities = null;
        this.created_at = "";
        this.retweeted_status = null;
        this.retweet_count = 0;
        this.quoted_status = null;
        this.quote_count = 0;
    }

    @Override
    public String toString()
    {
        return "{id=" + id_str + ", user=" + user + ", text=\"" + text + "\", entities=" + entities + ", created_at=" + created_at + ", retweeted_status=" + retweeted_status + ", rt=" + retweet_count + ", quoted_status=" + quoted_status + ", quoted_count=" + quote_count + "}";
    }
}
