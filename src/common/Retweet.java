/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les informations des Tweets traités
**/

package common;

import java.io.Serializable;

public class Retweet implements Serializable
{
    private static final long serialVersionUID = 41111l;
    public final String id_str;
    public final User user;
    public final String text;
    public final String created_at;
    public final int retweet_count;
    public final int quote_count;

    public Retweet()
    {
        this.id_str = "";
        this.user = null;
        this.text = "";
        this.created_at = "";
        this.retweet_count = 0;
        this.quote_count = 0;
    }

    @Override
    public String toString()
    {
        return "{id=" + id_str + ", user=" + user + ", text=" + text + ", created_at=" + created_at + ", rt=" + retweet_count + ", quoted_count=" + quote_count + "}";
    }
}
