import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.plaf.metal.MetalIconFactory.TreeControlIcon;

public class Kernel
{
    private List<Processo> filaDeProcessos;
    private long passoDeExecução = 0;
    enum PoliticaDePrioridade { PRIORIDADE_COM_PREEMPCAO, ROUND_ROBIN };
    private PoliticaDePrioridade politicaDoEscalonador;

    public Kernel(String[] args)
    {
        List<String> listaDeParametros = new ArrayList<>(Arrays.asList(args));

        for (int i = 0; i < args.length; i++)
        {
            switch (args[i])
            {
                case "-p":
                case "-P":
                    String complemento;
                    try {
                        complemento = args[i + 1];
                    } catch (Exception e) {
                        throw e;
                    }
                    if (politicaDoEscalonador == null)
                    {
                        switch (complemento)
                        {
                            case "pp":
                            case "PP":
                                this.politicaDoEscalonador = PoliticaDePrioridade.PRIORIDADE_COM_PREEMPCAO;
                                break;
                            case "rr":
                            case "RR":
                                this.politicaDoEscalonador = PoliticaDePrioridade.ROUND_ROBIN;
                                break;
                            default:
                                throw new InvalidParameterException("Parametro -p invalido");
                        }
                    }
                    else
                    {
                        throw new InvalidParameterException("Politica do escalonador ja definida.");
                    }
                    break;
            }
            
        }

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