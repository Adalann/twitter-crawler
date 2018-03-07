/**
*   @author Theo Martos
*   @author Jules Perret
**/

import java.util.*;
import java.io.*;
import java.net.*;
import twitter4j.*;

public class Indexor
{
    public static String ADDRESS;
    public final static int PORT = 2200;

    public static void main(String[] args)
    {
        IndexorThread indexor = null;
        boolean state = true;
        Scanner sc = new Scanner(System.in);

        if(args.length != 1)
        {
            System.out.println("Usage : java Indexor <address>");
        }
        else
        {
            try
            {
                ADDRESS = args[0];
                Socket connection = new Socket(ADDRESS, PORT);
                indexor = new IndexorThread(connection);
                indexor.start();
                String query;

                while(state && !connection.isClosed())
                {
                    System.out.print("> ");
                    query = sc.nextLine();

                    switch(query)
                    {
                        case "help":
                        {
                            System.out.println("help : show this help\nindex : show the number of tweets processed by this indexor\nstop : stop this indexor");
                            break;
                        }
                        case "index":
                        {
                            System.out.println(indexor.getIndex());
                            break;
                        }
                        case "stop":
                        {
                            indexor.close();
                            state = false;
                            break;
                        }
                        default:
                        {
                            System.out.println("Type 'help' to print the available commands");
                        }
                    }
                }
                if(connection.isClosed())
                    System.out.println("Connection closed by the Crawler Server.");
            }
            catch(IOException e)
            {
                e.printStackTrace(System.err);
            }
            finally
            {
                indexor.close();
                sc.close();
            }
        }
    }
}
