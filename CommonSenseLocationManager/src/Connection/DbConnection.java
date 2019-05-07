package Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import Control.Configuration;
import java.sql.Connection;

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

    //Gets the credentials and connection values from the config in order to connect to the server and database
    public DbConnection(Configuration config){
        this.host = config.getHost();
        this.user = config.getUser();
        this.dbUser = config.getDbUser();
        this.dbPass = config.getDbPass();
        this.lport = new Integer(config.getLport());
        this.rport = new Integer(config.getRport());
        this.url = config.getUrl();
        this.key = config.getKey();
    }

    //Allows access to the Connection class' PreparedStatement creation through this class
    public PreparedStatement prepareStatement(String statement) throws SQLException{
        return conn.prepareStatement(statement);
    }

    //Allows access to the Connection's commit method
    public void commit() throws SQLException{
        conn.commit();
    }

    /**
     * Connect to the server via ssh and access MySQL
     * @throws Exception
     */
   public void connect() throws JSchException, ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
       //Establish SSH connection to server
       java.util.Properties sessionConfig = new java.util.Properties();
       sessionConfig.put("StrictHostKeyChecking", "no");
       JSch jsch = new JSch();
       jsch.addIdentity(key);
       //System.out.println("Found file");
       session = jsch.getSession(user, host);
       session.setConfig(sessionConfig);
       session.connect();
       //System.out.println("Connected");

       //Connect to the MySQL Database
       int assigned_port = session.setPortForwardingL(lport, host, rport);
       Class.forName(driverName).newInstance();
       conn = DriverManager.getConnection(url, dbUser, dbPass);
       conn.setAutoCommit(false);
       //System.out.println("Database Accessed");
   }

   //Close the connection and session
   public void disconnect() throws SQLException{
       conn.close();
       session.disconnect();
       //TODO REMOVE ME
       //System.out.println("Closed Connections");
   }

}
