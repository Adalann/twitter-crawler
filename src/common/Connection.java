/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui représente les connection avec les indexeurs et qui permet de gérer les flux associés.
*/

package common;

import java.io.*;
import java.net.*;

public abstract class Connection extends Thread
{
    protected Socket connection;
    protected boolean state;

    public abstract void run();

    public abstract void close();

    public abstract String toString();
}
