import java.util.EnumMap;

public class Teste {
    public static void main(String[] args) {
        EnumMap<EstadoProcesso, Integer> m = new EnumMap<>(EstadoProcesso.class);
        m.computeIfPresent(EstadoProcesso.NEW, (k, v) -> v + 1);
        m.putIfAbsent(EstadoProcesso.NEW, 2);
        m.computeIfPresent(EstadoProcesso.NEW, (k, v) -> v + 1);
        m.putIfAbsent(EstadoProcesso.READY, 2);
        m.putIfAbsent(EstadoProcesso.RUNNING, 2);
        m.putIfAbsent(EstadoProcesso.EXIT, 2);
        m.putIfAbsent(EstadoProcesso.BLOCKED, 2);
        System.out.println(m);
        m.forEach((k, v) -> System.out.println("k=" + k + " v=" + v));
    }
}
