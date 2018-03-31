/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe Principale de l'analyser
**/

import analyser.*;

public class RunAnalyser
{
    public static void main(String[] args)
    {
        Analyser analyser = new Analyser();
        analyser.start();

        try
        {
            analyser.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
