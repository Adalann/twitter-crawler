/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les urls et les hashtags des tweets.
*/

package common;

import java.io.*;

public class Entities implements Serializable
{
    private static final long serialVersionUID = 44444l;
    public final Url[] urls;
    public final Hashtag[] hashtags;

    public Entities()
    {
        this.urls = new Url[0];
        this.hashtags = new Hashtag[0];
    }

    @Override
    public String toString()
    {
        return "{urls=" + urls + ", hashtags=" + hashtags + "}";
    }
}
