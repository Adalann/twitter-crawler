/**
*   @author Theo Martos
*   @author Jules Perret
**/

import java.util.*;
import java.io.*;
import java.net.*;

public class CrawlerServer extends Thread
{
    private static final int PORT = 2200;
    private ServerSocket server;
    private List<Connection> clients;
    private Garbage garbage;
    private boolean state;

    public CrawlerServer(Garbage g)
    {
        try
        {
            this.server = new ServerSocket(PORT);
            this.clients = new ArrayList<Connection>();
            this.garbage = g;
            this.state = true;
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void run()
    {
        while(state)
        {
            try
            {
                Connection c = new Connection(server.accept(), garbage);
                clients.add(c);
                c.start();
            }
            catch(IOException e)
            {
                if(state)
                    e.printStackTrace(System.err);
            }
        }
    }

    public void closeClients()
    {
        try
        {
            for(Connection c : clients)
            {
                c.close();
            }
            if(!this.server.isClosed())
                this.server.close();
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
        finally
        {
            this.state = false;
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
