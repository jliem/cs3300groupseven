package colab.test;

import colab.client.ColabClient;
import colab.server.ColabServer;

public class TestLaunch {
    
    public static void main(String[] args) throws Exception {
        ColabServer.main(new String[]{});
        Thread.sleep(1000L);
        ColabClient.main(new String[]{});
    }

}
