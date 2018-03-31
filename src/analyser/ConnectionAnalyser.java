/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui représente les connection avec les indexeurs et qui permet de gérer les flux associés.
*/

package analyser;

import java.io.*;
import java.net.*;
import common.*;

class ConnectionAnalyser extends Connection
{
    private static final int CONF_CODE = 2;
    private ObjectInputStream in;
    //private "objet qui encapsule les hashtables"

    public ConnectionAnalyser(Socket s)
    {
        this.connection = s;
        try
        {
            this.in = new ObjectInputStream(connection.getInputStream());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        this.state = false;
    }

    public void run()
    {
        state = true;
        while(state)
        {
            try
            {
                Tweet tweet = (Tweet)in.readObject();
                if(tweet != null)
                    System.out.println(tweet);
            }
            catch(ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void close()
    {
        try
        {
            in.close();
            if(!connection.isClosed())
                connection.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            state = false;
        }
    }

    /**
    *   Méthode toString, pour afficher les informations de l'instance de Connection.
    */
    @Override
    public String toString()
    {
        return this.getName() + " " + connection.toString();
    }

}
