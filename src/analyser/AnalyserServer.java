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
    private DataContainer dataContainer;
    private ConfigurationAnalyser conf;
    private boolean state;

    /**
    *   Constructeur de la classe, instancie les attributs
    *   @param d    Une instance de DataContainer
    */
    public AnalyserServer(DataContainer d)
    {
        this.conf = (ConfigurationAnalyser)ConfigFactory.getConf(CONF_CODE);
        this.clients = new ArrayList<ConnectionAnalyser>();
        this.dataContainer = d;
        try
        {
            this.server = new ServerSocket(conf.PORT);
            this.state = false;
        }
        catch(BindException e)
        {
            System.out.println(conf.ANSI_RED + "Impossible to use the port " + conf.PORT + conf.ANSI_RESET);
            e.printStackTrace(conf.ERROR_STREAM());
        }
        catch(IOException e)
        {
            e.printStackTrace(conf.ERROR_STREAM());
        }
        setName("AnalyserServer");
    }

    /**
    *   Méthode run héritée de Thread, écoute les connexions entrantes et crée les nouvelles Connection
    */
    @Override
    public void run()
    {
        System.out.println(conf.ANSI_GREEN + "Server started." + conf.ANSI_RESET);
        state = true;
        if(server != null)
        {
            while(state && (conf.CLIENT_LIMIT == -1 || (conf.CLIENT_LIMIT != -1 && clients.size() < conf.CLIENT_LIMIT)))
            {
                try
                {
                    ConnectionAnalyser client = new ConnectionAnalyser(server.accept(), dataContainer);
                    clients.add(client);
                    client.start();
                }
                catch(IOException e)
                {
                    if(state)
                    {
                        System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
                        e.printStackTrace(conf.ERROR_STREAM());
                    }
                }
            }
        }
        close();
    }

    /**
    *   Ferme le socket et toutes les connexions
    */
    public void close()
    {
        if(state) // Si le serveur a bien ete cree
        {
            try
            {
                for(ConnectionAnalyser c : clients)
                    c.close();
                if(!server.isClosed())
                    server.close();
            }
            catch(NullPointerException e)
            {
                // Si le serveur etait null
                e.printStackTrace(conf.ERROR_STREAM());
            }
            catch(IOException e)
            {
                System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
                e.printStackTrace(conf.ERROR_STREAM());
            }
            finally
            {
                state = false;
            }
        }
    }

    /**
    *   Affichie la liste des clients
    */
    public void listClients()
    {
        for(Connection c : clients)
            System.out.println(c.toString());
    }
}
