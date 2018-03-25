/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les constantes du programme.
*/

package common;

public class ConfigurationIndexor
{
    public final String HOSTNAME_CRAWLER;
    public final int PORT_CRAWLER;
    public final String HOSTNAME_ANALYSER;
    public final int PORT_ANALYSER;

    public ConfigurationIndexor()
    {
        this.HOSTNAME_CRAWLER = "";
        this.PORT_CRAWLER = -1;
        this.HOSTNAME_ANALYSER = "";
        this.PORT_ANALYSER = -1;
    }

    @Override
    public String toString()
    {

    }
}
