/**
*   @author Theo Martos
*   @author Jules Perret
**/

import java.util.*;
import java.io.*;
import java.net.*;
import twitter4j.*;

public class TwitterCrawler
{
    public static void main(String[] args)
    {
        boolean state = true;
        Crawler crawler = new Crawler();
        Garbage tweets = crawler.getGarbage();
        CrawlerServer server = new CrawlerServer(tweets);
        Scanner sc = new Scanner(System.in);

        PrintStream console = System.out;

        String f = "#PMQs";

        console.println("Starting the crawler...");
        crawler.start(f);
        console.println("Crawler started !");

        try
        {
            Thread.sleep(5000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace(System.err);
        }

        console.println("Starting the server...");
        server.start();
        console.println("Server started !");

        while(state)
        {
            console.print("> ");
            String query = sc.nextLine().toLowerCase();

            switch(query)
            {
                case "listcl":
                {
                    server.listClients();
                    break;
                }
                case "save":
                {
                    tweets.save();
                    break;
                }
                case "startc":
                {
                    crawler.start(f);
                    try
                    {
                        Thread.sleep(5000);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace(System.err);
                    }
                    break;
                }
                case "stopc":
                {
                    crawler.stop();
                    break;
                }
                case "stop":
                {
                    state = false;
                    break;
                }
                case "size":
                {
                    console.println(crawler.getGarbageSize());
                    break;
                }
                case "help":
                {
                    console.println("help : show this help\nsave : save the tweets already captured in a output.data file\nsize : print the number of tweets captured so far\nstartc : start the crawler\nstopc : stop the crawler\nstop : stop the application and save the data");
                    break;
                }
                default:
                {
                    console.println("Type 'help' to print the available commands");
                }
            }
        }

        crawler.stop();
        sc.close();
        server.closeClients();
        System.exit(0);
    }
}
