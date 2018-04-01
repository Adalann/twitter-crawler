/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe Principale du module d'analyse
**/

package analyser;

import common.*;

public class Analyser extends Thread
{
    private static final int CONF_CODE = 2;
    private ConfigurationAnalyser conf;
    private AnalyserServer server;

    public Analyser()
    {
        this.conf = (ConfigurationAnalyser)ConfigFactory.getConf(CONF_CODE);
        this.server = new AnalyserServer();
    }

    public void run()
    {
        server.start();

        try
        {
            server.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace(conf.ERROR_STREAM());
        }
    }

}
