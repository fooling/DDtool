import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author foooling@gmail.com
 *         13-8-29
 */
public class RandomTable {
    private final ArrayList<String> fieldTypeArray;
    private final ArrayList<String> fieldNameArray;
    private ArrayList<Object> lastRecord;
    private final String tableName;
    private static final String[] TYPES={"int","string","text"};
    public final static Map<String,String> typeToCreate = new HashMap<String,String>() {{
        put("int", "INT(11)");
        put("string", "VARCHAR(255)");
        put("text","TEXT");
    }};
    public final static Map<String,Integer> typeToReflect = new HashMap<String,Integer>() {{
        put("int", 1);
        put("string", 2);
        put("text",2);
    }};
    private RandomGenerator gen=new RandomGenerator();


    public RandomTable(int num) throws UnsupportedOperationException{
        if (num<1){
            throw new UnsupportedOperationException("Num out of range");
        }
        fieldTypeArray=new ArrayList<String>();
        fieldNameArray=new ArrayList<String>();
        tableName=gen.randString(6);
        for (int i=0;i<num;i++){
            fieldTypeArray.add(TYPES[gen.randInt(TYPES.length)]);
            //FIXME: may conflict in field name with a tiny possibility
            fieldNameArray.add(gen.randString(5));
        }
    }
    public String initTableSQL(){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(\n");
        for(int i=0;i<fieldTypeArray.size();i++){
            stringBuilder.append(" ").append(fieldNameArray.get(i)).append(" ").append(typeToCreate.get(fieldTypeArray.get(i))).append(" NOT NULL");
            if (i!=fieldTypeArray.size()-1){
                stringBuilder.append(",\n");
            }
        }
        stringBuilder.append("\n");
        stringBuilder.append(") ENGINE=INNODB;");
        return stringBuilder.toString();
    }

    public String insertSQL(){

        lastRecord=new ArrayList<Object>();
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("INSERT INTO `").append(tableName).append("` ( ");
        for (int i=0;i<fieldNameArray.size();i++){
            stringBuilder.append("`").append(fieldNameArray.get(i)).append("`");
            if (i!=fieldNameArray.size()-1){
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(" ) VALUES ( ");
        for (int i=0;i<fieldTypeArray.size();i++){
            stringBuilder.append("'");

            Object tmpRandOb=null;
            //TODO: was intend to use reflect
            switch (typeToReflect.get(fieldTypeArray.get(i))){

                case 1:
                    tmpRandOb=gen.randInt();
                    stringBuilder.append(tmpRandOb);
                    lastRecord.add(tmpRandOb);
                    break;
                case 2:
                    tmpRandOb=gen.randString(gen.randInt(255));
                    stringBuilder.append(tmpRandOb);
                    lastRecord.add(tmpRandOb);
                    break;
                default:
                    break;

            }
            stringBuilder.append("'");
            if (i!=fieldTypeArray.size()-1){
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(" );");
        return stringBuilder.toString();

    }

    public String dropSQL(){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("DROP TABLE ").append(tableName);
        return stringBuilder.toString();
    }
    public String selectSQL(){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("SELECT * FROM ").append(tableName).append(";");
        return stringBuilder.toString();
    }
    public String updateSQL(){
        StringBuilder stringBuilder=new StringBuilder();
        StringBuilder condionBuilder=new StringBuilder();

        int condition=gen.randInt(fieldNameArray.size());
        condionBuilder.append("`").append(fieldNameArray.get(condition)).append("` = '").append(lastRecord.get(condition)).append("'");

        stringBuilder.append("UPDATE ").append(tableName).append(" SET ");
        Object tmpRandOb=null;
        for (int i=0;i<fieldNameArray.size();i++){
            stringBuilder.append('`').append(fieldNameArray.get(i)).append("`= '");
            //TODO: was intend to use reflect
            switch (typeToReflect.get(fieldTypeArray.get(i))){

                case 1:
                    tmpRandOb=gen.randInt();
                    stringBuilder.append(tmpRandOb);
                    lastRecord.set(i,tmpRandOb);
                    break;
                case 2:
                    tmpRandOb=gen.randString(gen.randInt(255));
                    stringBuilder.append(tmpRandOb);
                    lastRecord.set(i, tmpRandOb);
                    break;
                default:
                    break;

            }
            stringBuilder.append("'");
            if (i!=fieldNameArray.size()-1){
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(" WHERE ");
        stringBuilder.append(condionBuilder);
        return stringBuilder.toString();
    }
    public ArrayList<String> getMeta(){
        return fieldTypeArray;
    }


}
