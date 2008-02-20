package colab.client;

import java.rmi.Naming;

import colab.community.CommunityName;
import colab.server.ColabServerInterface;
import colab.server.ConnectionInterface;
import colab.user.UserName;

public class ColabClient {

    public ColabClient() {
        
    }
    
    public static void main(String[] args) throws Exception {

        // Assign a security manager, in the event that dynamic classes are loaded.
        //if (System.getSecurityManager() == null) {
        //    System.setSecurityManager(new RMISecurityManager());
        //}
        
        int port = 9040;
        
        String url = "//localhost:" + port + "/COLAB_SERVER";
        
        ColabServerInterface server = (ColabServerInterface) Naming.lookup(url);
        ConnectionInterface connection = server.connect();
        boolean correct = connection.logIn(new UserName("Chris"), "pass4");
        if (correct) {
            System.out.println("User logged in.");
            correct = 
                connection.logIn(new CommunityName("Team Awesome"), "awesomePass");
            if (correct) {
                System.out.println("Logged into community.");
            } else {
                System.out.println("Community login failed.");
            }
        } else {
            System.out.println("Login failed.");
        }
        
    }
    
}
