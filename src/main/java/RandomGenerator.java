import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * @author foooling@gmail.com
 */
public class RandomGenerator {
    private static final String alpha="abcdefghijklmnopqrstuvwxyz";
    private static final int MAX_INT=999999;
    private Random random=new Random();
    public int randInt(){
        return randInt(MAX_INT);
    }
    public int randInt(int num){
        return random.nextInt(num);
    }
    public char randChar(){
        return alpha.charAt(randInt()%26);
    }
    public String randString(int length){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<length;i++){
            stringBuilder.append(randChar());
        }
        return stringBuilder.toString();
    }
}
