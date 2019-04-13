public class Driver {

    public static void main(String args[]){

        DbConnection conn = new DbConnection();

        try {
            conn.connect();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
