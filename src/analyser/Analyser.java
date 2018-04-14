/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe Principale du module d'analyse
**/

package analyser;

import common.*;
import java.io.*;
import java.util.*;

public class Analyser extends Thread
{
    private static final int CONF_CODE = 2;
    private ConfigurationAnalyser conf;
    private AnalyserServer server;
    private DataContainer dataContainer;
    private HITS hits;
    private Scanner sc;

    public Analyser()
    {
        this.conf = (ConfigurationAnalyser)ConfigFactory.getConf(CONF_CODE);
        this.dataContainer = new DataContainer();
        this.server = new AnalyserServer(dataContainer);
        this.hits = new HITS(dataContainer, 10);
        this.sc = new Scanner(System.in);
    }

    public void run()
    {
        server.start();

        while(true)
        {
            System.out.print("> ");
            String q = sc.readLine();
            if(q.equals("start"))
                hits.start();
        }

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
