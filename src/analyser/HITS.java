/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui implémente l'algorithme HITS
**/

package analyser;

import java.util.*;

class HITS extends Thread
{
    private static final int CONF_CODE = 2;
    private DataContainer data;
    private List<UserHITS> users;
    private int K;

    public HITS(DataContainer d, int k)
    {
        this.data = d;
        this.users = new ArrayList<UserHITS>();
        this.K = k;
    }

    public void run()
    {
        for(String id : data.getIDS())
            users.add(new UserHITS(id));

        double norm = 0;
        for(int i = 0; i < K; i++)
        {
            for(UserHITS user : users)
            {
                user.auth = 0;
                for(UserHITS neighbor : generateIncomingNeighbors(user.id))
                    user.auth += neighbor.hub;
                norm += user.auth * user.auth;
            }
            norm = Math.sqrt(norm);
            for(UserHITS user : users)
                    user.auth = user.auth / norm;
            norm = 0;

            for(UserHITS user : users)
            {
                user.hub = 0;
                for(UserHITS neighbor : generateOutcomingNeighbors(user.id))
                    user.hub += neighbor.auth;
                norm += user.hub * user.hub;
            }
            norm += Math.sqrt(norm);
            for(UserHITS user : users)
                user.hub = user.hub / norm;

            displayResults(i);
        }
    }

    private List<UserHITS> generateIncomingNeighbors(String idUser)
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

    public List<UserHITS> generateOutcomingNeighbors(String idUser)
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

    public void displayResults(int i)
    {
        System.out.println("STEP " + i);
        for(UserHITS user : users)
        {
            System.out.println(user);
        }
    }

    class UserHITS
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
        public String toString()
        {
            return name + " :\n\tAuth score : " + auth + "\n\tHub score : " + hub + "\n\n";
        }
    }
}
