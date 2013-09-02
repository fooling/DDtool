import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.*;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author foooling@gmail.com
 */
public class MainExample {


    public static void main(String[] args){
        final long timeout=1000*60*60*
                4; //hours
        String url="jdbc:mysql://localhost/adbcjtck";
        String user="adbcjtck";
        String password="adbcjtck";
        DruidDataSource druidDataSource=new DruidDataSource();
        String driver="com.mysql.jdbc.Driver";
        //String driver="org.adbcj.dbcj.Driver";
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(user);
        druidDataSource.setPassword(password);
        druidDataSource.setMaxActive(4);
        druidDataSource.setMinIdle(2);
        druidDataSource.setInitialSize(4);
        druidDataSource.setMaxWait(50000);
        druidDataSource.setValidationQuery("select 'x'");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);

        try{
            druidDataSource.init();
        } catch (Exception e){
            e.printStackTrace();
        }
        Connection conn=null;






        ThreadPoolExecutor es=new ThreadPoolExecutor(16, 20, 2000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(20),new ThreadPoolExecutor.AbortPolicy());
        CompletionService<Integer> cs=new ExecutorCompletionService<Integer>(es);

        //ArrayList<Thread> threadArrayList=new ArrayList<Thread>();
        long a=System.currentTimeMillis();
        try{


            for (int i=0;i<16;i++){
                cs.submit(new TaskToRun(druidDataSource.getConnection()));
            }
            while(System.currentTimeMillis()-a<timeout){
                cs.take();
                cs.submit(new TaskToRun(druidDataSource.getConnection()));
            }
            System.out.println("finished ,"+(System.currentTimeMillis()-a));


        } catch (Exception e){
            e.printStackTrace();
        }





    }
}
