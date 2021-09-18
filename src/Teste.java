import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Teste {
    public static void main(String[] args) {
        String APP_PATH = "apps";

        System.out.println(Files.exists(Paths.get(APP_PATH, "prog1.txt")));
        System.out.println(Files.isReadable(Paths.get(APP_PATH, "prog1.txt")));
        
        
    }
    
}
