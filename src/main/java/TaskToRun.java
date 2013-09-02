import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * @author foooling@gmail.com
 *         13-8-29
 */
public class TaskToRun implements Callable{
    private Connection conn;
    public TaskToRun(Connection conn){
        this.conn=conn;
    }
    private void initTable(Connection connection,String sql) throws Exception{
        PreparedStatement pstmt=connection.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();
    }
    private void insertValues(Connection connection,String sql) throws Exception{

        PreparedStatement preparedStatement=connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
    }
    private void checkResults(Connection connection,String sql,ArrayList<String> meta) throws Exception{
        ResultSet resultSet=null;
        PreparedStatement preparedStatement=connection.prepareStatement(sql);

        StringBuilder stringBuilder=new StringBuilder();
        Object result=null;
        try {
            resultSet=preparedStatement.executeQuery();
        } catch (Exception e){
            throw new Exception("Cannot get result set");
        }
        while(resultSet.next()){
            for (int i=0;i<meta.size();i++){
                if (meta.get(i).equals("int")){
                    result=resultSet.getInt(i+1);
                } else{
                    result=resultSet.getString(i+1);
                }
                stringBuilder.append(result);
                if (i!=meta.size()-1){
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append("\n");
            System.out.println(stringBuilder.toString());

        }
    }

    private void clearTable(Connection connection,String sql) throws Exception{
        PreparedStatement preparedStatement=connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
    }
    @Override
    public Integer call() {
        RandomGenerator gen=new RandomGenerator();
        RandomTable table=new RandomTable(gen.randInt(6)+1);
        try{
            initTable(conn,table.initTableSQL());
            for(int i=0;i<gen.randInt(30)+1;i++){
                insertValues(conn,table.insertSQL());
            }
            checkResults(conn,table.selectSQL(),table.getMeta());
            for(int i=0;i<gen.randInt(30)+1;i++){
                insertValues(conn, table.updateSQL());
            }
            checkResults(conn,table.selectSQL(),table.getMeta());
            clearTable(conn,table.dropSQL());
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
          return 1;
        }

    }
}
