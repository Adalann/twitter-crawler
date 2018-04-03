/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui décrit les urls.
*/

package common;

import java.io.*;

public class Url implements Serializable
{
    private static final long serialVersionUID = 45555l;
    public final String url;

    public Url()
    {
        this.url = "";
    }

    @Override
    public String toString()
    {
        return "{value=" + url + "}";
    }
}
