/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui implémente l'algorithme HITS
**/

package analyser;

import common.*;
import java.util.*;
import java.io.*;

class HITS extends Thread
{
    private static final int CONF_CODE = 2;
    private ConfigurationAnalyser conf;
    private DataContainer data;
    private List<UserHITS> users;
    private boolean compareMode; // Boolean qui indique si on compare par rapport au score de auth ou de hub => false pour auth, true pour hub

    /**
    *   Constructeur de la classe, initialise les attributs
    */
    public HITS(DataContainer d)
    {
        this.conf = (ConfigurationAnalyser)ConfigFactory.getConf(CONF_CODE);
        this.data = d;
        this.users = new ArrayList<UserHITS>();
        this.compareMode = false;
        setName("HITS");
    }

    /**
    *   Lance HITS
    */
    @Override
    public void run()
    {
        // On construit la liste d'utilisateur a partir des donnees de data
        for(String id : data.getIDS())
            users.add(new UserHITS(id));

        double norm = 0;
        // Le nombre de passage est definit par l'tulisateur dans les fichiers de configurations
        for(int i = 0; i < conf.HITS_REPETITION; i++)
        {
            // On calcule les scores de auth
            for(UserHITS user : users)
            {
                user.auth = 0;
                for(UserHITS neighbor : fetchIncomingNeighbors(user.id))
                    user.auth += neighbor.hub;
                norm += user.auth * user.auth;
            }
            // norm permet de normaliser les scores, de cette maniere les resultats convergent
            norm = Math.sqrt(norm);
            for(UserHITS user : users)
                    user.auth = user.auth / norm;
            norm = 0;

            // On calcule les scores de hub
            for(UserHITS user : users)
            {
                user.hub = 0;
                for(UserHITS neighbor : fetchOutcomingNeighbors(user.id))
                    user.hub += neighbor.auth;
                norm += user.hub * user.hub;
            }
            // norm permet de normaliser les scores, de cette maniere les resultats convergent
            norm += Math.sqrt(norm);
            for(UserHITS user : users)
                user.hub = user.hub / norm;
        }

        System.out.println(conf.ANSI_BLUE + "HITS done." + conf.ANSI_RESET);
    }

    /**
    *   Récupère la liste des IncomingNeighbors d'un utilisateur
    *   @param idUser L'ID d'un utilisateur
    *   @return       La liste de ses IncomingNeighbors
    */
    private List<UserHITS> fetchIncomingNeighbors(String idUser)
    {
        List<UserHITS> incomingNeighbors = new ArrayList<UserHITS>();
        List<String> dataFromContainer = data.getIncomingNeighbors(idUser);
        if(dataFromContainer != null)
        {
            for(String id : dataFromContainer)
                incomingNeighbors.add(new UserHITS(id));
        }

        return incomingNeighbors;
    }

    /**
    *   Récupère la liste des OutcomingNeighbors d'un utilisateur
    *   @param idUser L'ID d'un utilisateur
    *   @return       La liste de ses OutcomingNeighbors
    */
    private List<UserHITS> fetchOutcomingNeighbors(String idUser)
    {
        List<UserHITS> outcomingNeighbors = new ArrayList<UserHITS>();
        List<String> dataFromContainer = data.getOutcomingNeighbors(idUser);
        if(dataFromContainer != null)
        {
            for(String id : data.getOutcomingNeighbors(idUser))
                outcomingNeighbors.add(new UserHITS(id));
        }

        return outcomingNeighbors;
    }

    /**
    *   Ecrit les résulats de HITS
    */
    private void writeResults()
    {

        PrintWriter writer = null;
        // On definit le nom du fichier en fonction du mode de tri
        String filename = "";
        if(compareMode)
            filename = "../hubResults.txt";
        else
            filename = "../authResults.txt";

        try
        {
            // On initialise le flux et on ecrit les resultats
            writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
            for(UserHITS user : users)
                writer.println(user);
            System.out.println(conf.ANSI_GREEN + "Successfully generated results !" + conf.ANSI_RESET);
        }
        catch(IOException e)
        {
            e.printStackTrace(conf.ERROR_STREAM());
        }
        finally
        {
            if(writer != null)
                writer.close();
        }
    }

    /**
    *   Passe en mode auth
    */
    public void authSortedResults()
    {
        compareMode = false;
        Collections.sort(users);
        writeResults();
    }

    /**
    *   Passe en mode hub
    */
    public void hubSortedResults()
    {
        compareMode = true;
        Collections.sort(users);
        writeResults();
    }

    class UserHITS implements Comparable<UserHITS>
    {
        String id;
        String name;
        double auth;
        double hub;

        public UserHITS(String id)
        {
            this.id = id;
            this.name = data.getName(id);
            this.auth = 1.;
            this.hub = 1.;
        }

        public boolean equals(UserHITS u)
        {
            return this.id.equals(u.id);
        }

        @Override
        public int compareTo(UserHITS u)
        {
            int result;
            if(compareMode) // Mode hub
            {
                if(this.hub > u.hub) // On veut trier de manière décroissante pour que le plus grand soit en haut de la liste
                    result = -1;
                else if(this.hub == u.hub)
                    result = 0;
                else
                    result = 1;
            }
            else
            {
                if(this.auth > u.auth) // Meme remarque
                    result = -1;
                else if(this.auth == u.auth)
                    result = 0;
                else
                    result = 1;
            }

            return result;
        }

        @Override
        public String toString()
        {
            return name + " => Auth score : " + auth + " | Hub score : " + hub;
        }
    }
}
