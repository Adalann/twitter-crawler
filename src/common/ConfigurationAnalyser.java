/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui contient les constantes du programme.
*/

package common;

public class ConfigurationAnalyser extends Configuration
{
    public final int PORT;
    public final int CLIENT_LIMIT;

    public ConfigurationAnalyser()
    {
        this.PORT = -1;
        this.CLIENT_LIMIT = -1;
    }

    @Override
    public String toString()
    {
        return "Port : " + PORT + "\n\tClient limit : " + CLIENT_LIMIT;
    }
}
