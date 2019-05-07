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

    //Returns an instance of the configuration information
    public static Configuration getInstance(){
        if (instance == null){
            instance = new Configuration();
        }
        return instance;
    }

    //Reads the config.ini file for the connection values
    //Returns true if the file is read successfully
    //Returns false if the file is not read
    public boolean readConfig() throws IOException {
        //Sets the path of the config folder to the users home directory .CommonSenseDB
        String configDirectory = System.getProperty("user.home") + File.separator + ".CommonSenseDB";

        //Attempts to make the directory
        File dir = new File(configDirectory);
        //Establishes the path to the config.ini file and checks if the file already exists
        File path = new File(configDirectory + File.separator + "config.ini");
        //If the file does not exist, creates the directory and returns false
        if (!path.isFile()){
            dir.mkdir();
            return false;
        }
        else {
            //If the file does exist, reads and stores the configuration values
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
