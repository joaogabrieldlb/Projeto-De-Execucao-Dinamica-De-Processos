import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Kernel
{
    enum PoliticaDePrioridade { PRIORIDADE_COM_PREEMPCAO, ROUND_ROBIN };
    public static int PRIORIDADE_PADRAO = 2;
    private long pidCounter = 1;
    private final String appsPath;
    private PoliticaDePrioridade politicaDoEscalonador;
    private int quantum;
    private List<Processo> filaDeProcessos = new ArrayList<>();
    private long passoDeExecucao = 0;

    public Kernel(String[] args)
    {
        this.appsPath = Startup.appsDirectory;
        // Verificacao de existencia de argumentos
        if (args.length == 0)
        {
            throw new ExceptionInInitializerError("OS sem argumentos. Saindo.");
        }
        
        // Tratamento de argumentos para UpperCase
        for (int i = 0; i < args.length; i++)
        {
            args[i] = args[i].toUpperCase();
        }
        List<String> listaDeParametros = new ArrayList<>(Arrays.asList(args));
        // DEBUG
        if (Startup.teste) System.out.println("ARGS: "+ listaDeParametros);
        
        // DEBUG - mostra index do -p
        if (Startup.teste) System.out.println("INDICE DO PARAMETRO -P: " + listaDeParametros.indexOf("-P"));

        // Definicao da politica de escalonamento (-P)
        int indexP = listaDeParametros.indexOf("-P");
        if (indexP < 0)
        {
            throw new InvalidParameterException("Parametro -p nao encontrado.");
        }
        // Configuracao da politica de escalonamento
        String complementoP;
        try {
            complementoP = listaDeParametros.get(indexP + 1);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidParameterException("Parametro -p sem complemento.");
        }
        if (politicaDoEscalonador == null)
        {
            switch (complementoP)
            {
                case "PP":
                    this.politicaDoEscalonador = PoliticaDePrioridade.PRIORIDADE_COM_PREEMPCAO;
                    //DEBUG
                    if (Startup.teste) System.out.println("POLITICA DO ESCALONADOR: " + politicaDoEscalonador);
                    break;
                case "RR":
                    this.politicaDoEscalonador = PoliticaDePrioridade.ROUND_ROBIN;
                    //DEBUG
                    if (Startup.teste) System.out.println("POLITICA DO ESCALONADOR: " + politicaDoEscalonador);
                    try {
                        this.quantum = Integer.parseInt(listaDeParametros.get(indexP + 2));
                        // DEBUG
                        if (Startup.teste) System.out.println("VALOR DO QUANTUM: " + this.quantum);
                        if (this.quantum <= 0) throw new InvalidParameterException("Valor menor ou igual a zero.");
                    } catch (Exception e) {
                        throw new InvalidParameterException("Quantum invalido. " + e.getMessage());
                    }
                    break;
                default:
                    throw new InvalidParameterException("Parametro -p com complemento invalido: \"" + complementoP + "\"");
            }
        }
        
        // Definicao da lista de processos (-L)
        int indexL = listaDeParametros.indexOf("-L");
        if (indexL < 0)
        {
            throw new InvalidParameterException("Parametro -l nao encontrado.");
        }
        // DEBUG
        if (Startup.teste) System.out.println("INDICE DO PARAMETRO -L: " + indexL);

        int indexLInicio;
        int indexLFim;
        for (int i = indexLInicio = indexLFim = indexL + 1; i < listaDeParametros.size(); i++)
        {
            try {
                String posicao = listaDeParametros.get(i);
                if (posicao.charAt(0) == '-') throw new IndexOutOfBoundsException("Fim do parametro -l.");
            } catch (Exception e) {
                indexLFim = i;
                break;
            }
        }
        // DEBUG
        if (Startup.teste) System.out.println("INDEXL INICIO: " + indexLInicio);
        if (Startup.teste) System.out.println("INDEXL FIM: " + indexLFim);

        List<String> listaDeProgramas = listaDeParametros.subList(indexLInicio, indexLFim);
        if (listaDeProgramas.isEmpty())
        {
            throw new InvalidParameterException("Lista de programas vazia.");
        }
        // DEBUG
        if (Startup.teste) System.out.println("LISTA DE PROGRAMAS: " + listaDeProgramas);

        // Configuaracao da fila de processos
        ListIterator<String> iterLP = listaDeProgramas.listIterator();
        while (iterLP.hasNext())
        {
            String parametro = iterLP.next();
            // DEBUG
            if (Startup.teste) System.out.println("PARAMETRO: " + parametro);

            if (Files.isReadable(Paths.get(appsPath, parametro)))
            {
                // DEBUG
                if (Startup.teste) System.out.println("PROGRAMA ACESSADO: " + parametro);
                Path arquivoDoPrograma = Paths.get(appsPath, parametro);
                int prioridade = PRIORIDADE_PADRAO;

                if (iterLP.hasNext())
                {
                    String complementoPrograma = iterLP.next();
                    switch (complementoPrograma)
                    {
                        case "0":
                        case "1":
                        case "2":
                            prioridade = Integer.parseInt(complementoPrograma);
                            break;
                        default:
                            iterLP.previous();
                    }
                }

                try {
                    filaDeProcessos.add(new Processo(pidCounter, arquivoDoPrograma, passoDeExecucao, prioridade, quantum));
                    pidCounter++;
                } catch (Exception e) {
                    System.err.println("Erro ao carregar " + parametro + ": " + e.getMessage());
                }
            }
            else
            {
                System.err.println("Erro ao carregar " + parametro + ": arquivo indisponivel.");
            }
        }
    }

    public void run ()
    {
        do
        {

            escalonador();
            filaDeProcessos.get(0).run();
            imprimeEstado();
            passoDeExecucao++;
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
        filaDeProcessos.sort((p1, p2) -> p1.getEstadoDoProcesso().compareTo(p2.getEstadoDoProcesso()));
        System.out.println(filaDeProcessos);
        
    }

    public void processo ()
    {

    }


}