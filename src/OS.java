import java.util.ArrayList;
import java.util.List;

public class OS
{
    private List<Processo> filaDeProcessos;
    private long passoDeExecução = 0;
    enum politicaDeEscalonamento { PRIORIDADE_COM_PREEMPCAO, ROUND_ROBIN };
    

    public OS(String[] args)
    {


    }

    public void run ()
    {
        do
        {

            imprimeEstado();
            passoDeExecução++;
        }
        while (!isTodosProcessosEncerrados());


    }

    private void imprimeEstado()
    {
        // TODO: Imprimir estado do OS e da aplicação
    }

    private boolean isTodosProcessosEncerrados ()
    {
        return filaDeProcessos.stream()
            .map(p -> p.getEstadoDoProcesso())
            .allMatch(ep -> ep.equals(EstadoProcesso.EXIT));
    }

    public void escalonador ()
    {

    }

    public void processo ()
    {

    }


}