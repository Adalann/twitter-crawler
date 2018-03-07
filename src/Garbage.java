/**
*   @author Theo Martos
*   @author Jules Perret
**/

import java.util.*;
import twitter4j.*;
import twitter4j.json.*;
import java.io.*;

public class Garbage
{
    private List<String> tweets;
    private int index;

    public Garbage()
    {
        this.tweets = new ArrayList<String>();
        this.index = 0;
    }

    public void addStringElement(String e)
    {
        tweets.add(e.replace('\n', ' '));
    }

    public void addStatusElement(Status e)
    {
        tweets.add(DataObjectFactory.getRawJSON(e.replace('\n', ' ')));
    }

    public synchronized void addStringCollection(List<String> c)
    {
        tweets.addAll(c);
    }

    public synchronized void addStatusCollection(List<Status> c)
    {
        for(Status status : c)
            tweets.add(status.toString());
    }

    public String getElementAt(int i)
    {
        if(i < tweets.size())
            return tweets.get(i);
        else
            return "";
    }

    public String getLastElement()
    {
        return getElementAt(tweets.size() - 1);
    }

    public synchronized String getNextElement()
    {
        String r = "";
        if(index < tweets.size())
            r = tweets.get(index++);
        return r;
    }

    public List<String> getList()
    {
        return tweets;
    }

    public synchronized int size()
    {
        return tweets.size();
    }

    public synchronized void save()
    {
        PrintWriter out = null;
        try
        {
            System.out.print("Writing data (" + tweets.size() + " tweets)... ");
            out = new PrintWriter(new BufferedWriter(new FileWriter("../jo.data")));
            for(String s : tweets)
                out.println(s + "\n");
            System.out.println("OK");

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
