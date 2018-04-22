/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe Principale du crawler
**/

import crawler.*;

public class RunCrawler
{
    public static void main(String[] args)
    {
        Crawler crawler = new Crawler();
        crawler.start();

        try
        {
            crawler.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
