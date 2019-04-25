package Control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class Configuration {

    private static Configuration instance = null;

    private int lport;
    private int rport;
    private String host;
    private String user;
    private String dbUser;
    private String dbPass;
    private String url;
    private String key;
    private String database;

    private Configuration(){

    }

    public static Configuration getInstance(){
        if (instance == null){
            instance = new Configuration();
        }
        return instance;
    }

    public boolean readConfig() throws IOException {
        String configDirectory = System.getProperty("user.home") + File.separator + ".CommonSenseDB";

        File dir = new File(configDirectory);
        File path = new File(configDirectory + File.separator + "config.ini");
        if (!path.isFile()){
            dir.mkdir();
            return false;
        }
        else {
            java.util.Properties config = new java.util.Properties();
            config.load(new FileInputStream(configDirectory + File.separator + "config.ini"));
            this.host = config.getProperty("host");
            this.user = config.getProperty("remoteUser");
            this.dbUser = config.getProperty("dbUser");
            this.dbPass = config.getProperty("dbPass");
            this.lport = new Integer(config.getProperty("lport"));
            this.rport = new Integer(config.getProperty("rport"));
            this.database = config.getProperty("database");
            this.url = "jdbc:mysql://localhost:" + lport + "/" + database;
            this.key = config.getProperty("key");

        }
        return true;
    }

    public String getHost(){
        return host;
    }

    public int getLport() {
        return lport;
    }

    public int getRport() {
        return rport;
    }

    public String getDbPass() {
        return dbPass;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getDatabase() {
        return database;
    }
}
