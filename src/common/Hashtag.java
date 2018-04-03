/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui décrit les urls.
*/

package common;

import java.io.*;

public class Hashtag implements Serializable
{
    private static final long serialVersionUID = 46666l;
    public final String text;

    public Hashtag()
    {
        this.text = "";
    }

    @Override
    public String toString()
    {
        return "{text=" + text + "}";
    }
}
