
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

   public class Conexion {
    
    private final String BD="videoclub";
    private final String url="jdbc:mysql://localhost:3306/"+BD;
    private final String usuario="root";
    private final String pass="";
    
    private static Conexion dataSource;
    private BasicDataSource basicDataSource=null;
            
    public Conexion(){
        basicDataSource=new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUsername(usuario);
        basicDataSource.setPassword(pass);
        basicDataSource.setUrl(url);
        
        basicDataSource.setMinIdle(5);
        basicDataSource.setMaxIdle(20);
        basicDataSource.setMaxTotal(50);
        basicDataSource.setMaxWaitMillis(10000);
    
    
}
   
    public static Conexion getInstance(){
        if(dataSource==null){
            dataSource= new Conexion();
            return dataSource;
        }else{
            return dataSource;
        }
    }
    public Connection getConnection() throws SQLException{
        return this.basicDataSource.getConnection();
    }
    
    public void closeConnection (Connection connection)throws SQLException{
        connection.close();
    }
   }
