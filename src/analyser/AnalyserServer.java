/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui gère les connexions avec les indexeurs pour recevoir les tweets traités
**/

package analyser;

import java.util.*;
import java.io.*;
import java.net.*;
import util.*;

public class AnalyserServer extends Thread
{
    private ServerSocket server;
    private List<Connectio> clients;

    private Configuration conf;
    private boolean state;

    public AnalyserServer()
    {
        conf = ConfigFactory.getConf();
        try
        {
            this.server = new ServerSocket(conf.PORT);
            this.state = false;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        state = true;
        while(state )
    }
}
