import java.io.*;
import java.nio.file.Files;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;

import java.sql.Connection;
import java.util.Properties;


public class DbConnection{

    int lport;
    int rport;
    String host;
    String user;
    String dbUser;
    String dbPass;
    String url;
    String key;
    String driverName = "com.mysql.cj.jdbc.Driver";
    Connection conn = null;
    Session session = null;

    public DbConnection(){
        try{
            readConfig();
        } catch (Exception e){

        }
    }


    /**
     * Connect to the server via ssh and access MySQL
     * @throws Exception
     */
   public void connect() throws  Exception {
       java.util.Properties sessionConfig = new java.util.Properties();
       sessionConfig.put("StrictHostKeyChecking", "no");
       JSch jsch = new JSch();
       jsch.addIdentity(key);
       System.out.println("Found file");
       session = jsch.getSession(user, host);
       session.setConfig(sessionConfig);
       session.connect();
       System.out.println("Connected");

       int assigned_port = session.setPortForwardingL(lport, host, rport);
       Class.forName(driverName).newInstance();
       conn = DriverManager.getConnection(url, dbUser, dbPass);
       System.out.println("Database Accessed");

       conn.close();
       session.disconnect();
   }

    /**
     * Reads the config file for ssh access credentials
     * @throws FileNotFoundException
     * @throws IOException
     */
   private void readConfig() throws FileNotFoundException, IOException {
       String configDirectory = System.getProperty("user.home") + File.separator + ".CommonSenseDB";

       File dir = new File(configDirectory);
       if (dir.mkdirs()){
           createConfig();
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
           this.url = "jdbc:mysql://localhost:" + lport + "/" + config.getProperty("database");
           this.key = config.getProperty("key");
       }
   }

    /**
     * Create the config file if it does not exist
     */
   private void createConfig(){
        //TODO : Add code to create config file is one did not already exist

   }
}
