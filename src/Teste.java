import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Teste {
    public static void main(String[] args) {
        System.out.println(args[0]);
        String APP_PATH = "apps";

        System.out.println(Files.exists(Paths.get(APP_PATH, "prog1.txt")));
        System.out.println(Files.isReadable(Paths.get(APP_PATH, "prog1.txt")));
        System.out.println(Character.isLetterOrDigit('#'));
        List<EstadoProcesso> es = new ArrayList<>();
        es.add(EstadoProcesso.BLOCKED);
        es.add(EstadoProcesso.EXIT);
        es.add(EstadoProcesso.READY);
        Collections.sort(es, (a, b) -> a.compareTo(b));
        System.out.println(es);

        RefMem p1 = new RefMem(0, "p1");
        RefMem p2 = new RefMem(1, "p2");
        RefMem p3 = new RefMem(1, "p3");
        RefMem p4 = new RefMem(1, "p4");
        RefMem p5 = new RefMem(2, "p5");
        RefMem p6 = new RefMem(2, "p6");
        RefMem p7 = new RefMem(2, "p7");
        RefMem p8 = new RefMem(3, "p8");

        Queue<RefMem> refMems = new LinkedBlockingQueue<RefMem>();

        refMems.add(p1);
        System.out.println(refMems);
        refMems.add(p2);
        System.out.println(refMems);
        refMems.add(p3);
        System.out.println(refMems);
        refMems.add(p4);
        System.out.println(refMems);
        refMems.add(p5);
        System.out.println(refMems);
        refMems.add(p6);
        System.out.println(refMems);
        refMems.add(p7);
        System.out.println(refMems);
        refMems.add(p8);
        System.out.println(refMems);
        
        // refMems.remove(p2);
        System.out.println(refMems.remove());
        System.out.println(refMems.remove());
        System.out.println(refMems.remove());
        System.out.println(refMems.remove());
        System.out.println(refMems.remove());
        System.out.println(refMems.remove());
        System.out.println(refMems.remove());
        System.out.println(refMems.remove());
        System.out.println(refMems);


    }
}
