import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Teste {
    public static void main(String[] args) {
        String APP_PATH = "apps";

        System.out.println(Files.exists(Paths.get(APP_PATH, "prog1.txt")));
        System.out.println(Files.isReadable(Paths.get(APP_PATH, "prog1.txt")));
        System.out.println(Character.isLetterOrDigit('#'));
        List<EstadoProcesso> es = new ArrayList<>();
        es.add(EstadoProcesso.BLOCKED);
        es.add(EstadoProcesso.EXIT);
        es.add(EstadoProcesso.READY);
        es.add(EstadoProcesso.TIMEOUT);
        Collections.sort(es, (a, b) -> a.compareTo(b));
        System.out.println(es);
        
    }
    
}
