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
        initUserList();
        double norm = 0;
        for(int i = 0; i < K; i++)
        {
            for(UserHITS user : users)
            {
                user.auth = 0;
                for(UserHITS neighbors : generateIncomingNeighbors(user.id))
                    user.auth += neighbors.hub;
                norm = user.auth * user.auth;
            }
            norm = math.sqrt(norm);
            for(UserHITS user : users)
                user.auth = user.auth / norm;
            norm = 0;

            for(UserHITS user : users)
            {
                user.hub = 0;
                for(UserHITS neighbors : generateOutcomingNeighbors(idUser))
                    user.hub += neighbors.auth;
                norm += user.hub * user.hub;
            }
            norm += math.sqrt(norm);
            for(UserHITS user : users)
                user.hub = user.hub / norm;
        }
    }

    public void initUserList()
    {
        for(String id : data.getIDS())
            users.add(new UserHITS(id));
    }

    private List<UserHITS> generateIncomingNeighbors(String idUser)
    {
        List<String> userTweets = data.getTweets(idUser);
        List<UserHITS> incomingNeighbors = new ArrayList<UserHITS>();

        for(String tweetId : userTweets)
        {
            for(UserHITS user : users)
            {
                if(data.getRetweets(user.id).contains(tweetId) && !incomingNeighbors.contains(user)) // si user à retweeter un tweet  de l'utilisateur identifié par userId et que cet utilisater n'est pas dejà dans la liste
                    incomingNeighbors.add(user);
            }
        }

        return incomingNeighbors;
    }

    public List<String> generateOutcomingNeighbors(String idUser)
    {
        return data.getRetweets(idUser);
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
            this.auth = 1;
            this.hub = 1;
        }

        @Override
        public boolean equals(UserHITS u)
        {
            return this.id.equals(u.id);
        }
    }
}
