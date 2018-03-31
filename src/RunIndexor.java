/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe Principale des modules indexeurs
**/

import indexor.*;

public class RunIndexor
{
    public static void main(String[] args)
    {
        Indexor indexor = new Indexor();
        indexor.start();

        try
        {
            indexor.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
