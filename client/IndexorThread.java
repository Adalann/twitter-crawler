/**
*   @author Theo Martos
*   @author Jules Perret
**/

import java.io.*;
import java.net.*;

public class IndexorThread extends Thread
{
    private String tweet;
    private Emetteur transmitter;
    private Content cont;
    private Reference ref;
    private Socket soc;
    private boolean state;
    private int index;

    private BufferedReader in;
    private PrintWriter out;

    public IndexorThread(Socket c)
    {
        this.tweet = "";
        this.transmitter = null;
        this.cont = null;
        this.ref = null;
        this.soc = c;
        this.state = true;
        this.index = 0;
        try
        {
            in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())), true);
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
            tweet = requestNextTweet();
            // System.out.println(tweet+"\n");
            try
            {
                Thread.sleep(1500);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public synchronized String requestNextTweet()
    {
        String s = "";
        try
        {
            out.println("NEXT");
            s = in.readLine();
            if(!s.equals(""))
                index++;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return s;
    }

    public synchronized void close()
    {
        try
        {
            out.println("STOP");
            in.close();
            out.close();
            soc.close();
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
}
