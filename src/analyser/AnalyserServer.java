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
import common.*;

class AnalyserServer extends Thread
{
    private static final int CONF_CODE = 2;
    private ServerSocket server;
    private List<ConnectionAnalyser> clients;
    //private "classe qui encapsule les hashtables"
    private ConfigurationAnalyser conf;
    private boolean state;

    public AnalyserServer()
    {
        this.conf = (ConfigurationAnalyser)ConfigFactory.getConf(CONF_CODE);
        this.clients = new ArrayList<ConnectionAnalyser>();
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
        while(state && (conf.CLIENT_LIMIT == -1 || (conf.CLIENT_LIMIT != -1 && clients.size() < conf.CLIENT_LIMIT)))
        {
            try
            {
                ConnectionAnalyser client = new ConnectionAnalyser(server.accept());
                clients.add(client);
                client.start();
            }
            catch(IOException e)
            {
                if(state)
                    e.printStackTrace(System.err);
            }
        }
    }

    public void close()
    {
        try
        {
            for(ConnectionAnalyser c : clients)
                c.close();
            if(!server.isClosed())
                server.close();
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
        finally
        {
            state = false;
        }
    }

    public void listClients()
    {
        for(Connection c : clients)
        {
            System.out.println(c.toString());
        }
    }
}
