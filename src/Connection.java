/**
*   @author Theo Martos
*   @author Jules Perret
**/

import java.io.*;
import java.net.*;

public class Connection extends Thread
{
    private Socket connection;
    private Garbage tweets;
    private BufferedReader in;
    private PrintWriter out;
    private boolean state;

    private static int Index = 1;

    public Connection(Socket s, Garbage g)
    {
        this.connection = s;
        this.tweets = g;
        this.setName("Client-" + Index++);
        try
        {
            this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())), true);
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
        this.state = true;
    }

    @Override
    public void run()
    {
        try
        {
            String query = "";
            while(state)
            {
                query = in.readLine();
                switch(query)
                {
                    case "NEXT":
                    {
                        out.println(tweets.getNextElement());
                        break;
                    }
                    case "STOP":
                    {
                        state = false;
                        break;
                    }
                }
            }
        }
        catch(IOException e)
        {
            if(state)
                e.printStackTrace(System.err);
        }
        finally
        {
            if(!connection.isClosed())
                close();
        }
    }

    public void close()
    {
        try
        {
            in.close();
            out.close();
            if(!connection.isClosed())
                connection.close();
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public String toString()
    {
        return this.getName() + " " + connection.toString();
    }

}
