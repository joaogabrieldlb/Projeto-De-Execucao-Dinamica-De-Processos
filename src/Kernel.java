import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Kernel
{
    // Definicao da politica de escalonamento
    enum PoliticaDeEscalonamento { PRIORIDADE_COM_PREEMPCAO, ROUND_ROBIN };
    private PoliticaDeEscalonamento politicaDoEscalonador;

    private long pidCounter = 1;
    private String appsPath;
 
    private List<Processo> listaDeProcessos = new ArrayList<>();
    
    // Caso a politica de escalonamento for Round Robin
    private Queue<Processo> filaProcessosProntosRR = new LinkedBlockingQueue<>();

    // Caso a politica de escalonamento for Prioridade Preemptivo
    private Queue<Processo> filaProcessosProntosAltaPrioridade = new LinkedBlockingQueue<>();
    private Queue<Processo> filaProcessosProntosMediaPrioridade = new LinkedBlockingQueue<>();
    private Queue<Processo> filaProcessosProntosBaixaPrioridade = new LinkedBlockingQueue<>();
    public static final int PRIORIDADE_PADRAO = 2;
    public static final int ARRIVAL_TIME_PADRAO = 0;

    private int passoDeExecucao = 0;

    public Kernel(String[] args)
    {
        this.appsPath = OS.appsDirectory;
        // Verificacao de existencia de argumentos
        if (args.length == 0)
        {
            throw new ExceptionInInitializerError("OS sem argumentos. Saindo.");
        }
        
        // Tratamento de argumentos para UpperCase
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].startsWith("-")) args[i] = args[i].toUpperCase();
        }
        List<String> listaDeParametros = new ArrayList<>(Arrays.asList(args));

        int indexV = listaDeParametros.indexOf("-V");
        if (indexV >= 0)
        {
            OS.verbose = true;
            System.out.println("=============== MODO VERBOSO ===============");
        }

        // DEBUG
        if (OS.verbose) System.out.println("ARGS: "+ listaDeParametros);
        
        // Definicao da politica de escalonamento (-P)
        int indexP = listaDeParametros.indexOf("-P");
        if (indexP < 0)
        {
            throw new InvalidParameterException("Parametro -p nao encontrado.");
        }
        
        // DEBUG - mostra index do -p
        if (OS.verbose) System.out.println("INDICE DO PARAMETRO -P: " + listaDeParametros.indexOf("-P"));


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
                case "pp":
                case "PP":
                    this.politicaDoEscalonador = PoliticaDeEscalonamento.PRIORIDADE_COM_PREEMPCAO;
                    //DEBUG
                    if (OS.verbose) System.out.println("POLITICA DO ESCALONADOR: " + politicaDoEscalonador);
                    break;
                case "rr":
                case "RR":
                    this.politicaDoEscalonador = PoliticaDeEscalonamento.ROUND_ROBIN;
                    //DEBUG
                    if (OS.verbose) System.out.println("POLITICA DO ESCALONADOR: " + politicaDoEscalonador);
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
        // DEBUG - mostra index do -l
        if (OS.verbose) System.out.println("INDICE DO PARAMETRO -L: " + indexL);

        int indexLInicio;
        int indexLFim;
        for (int i = indexLInicio = indexLFim = indexL + 1; i < listaDeParametros.size(); i++)
        {
            try {
                String posicao = listaDeParametros.get(i);
                if ((posicao.charAt(0) == '-')) throw new IndexOutOfBoundsException("Fim do parametro -l.");
                if (i == listaDeParametros.size() - 1)
                {
                    i++;
                    throw new IndexOutOfBoundsException("Fim da lista de parametros.");
                }
            } catch (Exception e) {
                indexLFim = i;
                break;
            }
        }
        // DEBUG
        if (OS.verbose) System.out.println("INDEXL INICIO: " + indexLInicio);
        if (OS.verbose) System.out.println("INDEXL FIM: " + indexLFim);

        List<String> listaDeProgramas = listaDeParametros.subList(indexLInicio, indexLFim);
        if (listaDeProgramas.isEmpty())
        {
            throw new InvalidParameterException("Lista de programas vazia.");
        }
        // DEBUG
        if (OS.verbose) System.out.println("LISTA DE PROGRAMAS: " + listaDeProgramas);

        // Configuaracao da fila de processos
        ListIterator<String> iterLP = listaDeProgramas.listIterator();
        while (iterLP.hasNext())
        {
            String parametro = iterLP.next();
            // DEBUG
            if (OS.verbose) System.out.println("PARAMETRO: " + parametro);

            if (Files.isReadable(Paths.get(appsPath, parametro)))
            {
                // DEBUG
                if (OS.verbose) System.out.println("PROGRAMA ACESSADO: " + parametro);
                Path arquivoDoPrograma = Paths.get(appsPath, parametro);
                Integer quantum = null;
                int prioridade = PRIORIDADE_PADRAO;
                int arrivalTime = ARRIVAL_TIME_PADRAO;

                // Define prioridade ou quantum
                if (iterLP.hasNext())
                {
                    String complementoPolitica = iterLP.next();
                    int complementoPoliticaInt = 0;
                    switch(this.politicaDoEscalonador)
                    {
                        case PRIORIDADE_COM_PREEMPCAO:
                            try {
                                complementoPoliticaInt = Integer.parseInt(complementoPolitica);
                                if (complementoPoliticaInt < 0) throw new InvalidParameterException("Valor nao pode ser negativo.");
                                if (complementoPoliticaInt > PRIORIDADE_PADRAO) throw new InvalidParameterException();
                                prioridade = complementoPoliticaInt;
                                // DEBUG
                                if (OS.verbose) System.out.println("VALOR DA PRIORIDADE: " + prioridade);
                            } catch (Exception e) {
                                iterLP.previous();
                                // System.err.println("Parametro prioridade invalido. " + e.getMessage() + "\nDefinido valor padrao: prioridade=" + PRIORIDADE_PADRAO);
                            }
                            break;
                        case ROUND_ROBIN:
                            try {
                                quantum = Integer.parseInt(complementoPolitica);
                                if (quantum <= 0) throw new InvalidParameterException("Valor menor ou igual a zero.");
                                // DEBUG
                                if (OS.verbose) System.out.println("VALOR DO QUANTUM: " + quantum);
                            } catch (Exception e) {
                                throw new InvalidParameterException("Parametro quantum invalido. " + e.getMessage());
                            }
                            break;
                        default:
                            break;
                    }
                }

                if(iterLP.hasNext())
                {
                    String complementoArrivalTime = iterLP.next();
                    try {
                        arrivalTime = Integer.parseInt(complementoArrivalTime);
                        if (arrivalTime < 0) throw new InvalidParameterException("Valor nao pode ser negativo.");
                        // DEBUG
                        if (OS.verbose) System.out.println("VALOR DO ARRIVAL_TIME: " + arrivalTime);
                    } 
                    catch(Exception e)
                    {
                        // System.err.println("Parametro arrival_time invalido. " + e.getMessage() + "\nDefinido valor padrao: arrival_time=" + ARRIVAL_TIME_PADRAO);
                        iterLP.previous();
                    }
                }

                try {
                    if (this.politicaDoEscalonador.equals(PoliticaDeEscalonamento.ROUND_ROBIN) && quantum == null) throw new InvalidParameterException("Valor do quantum nao encontrado para o processo \"" + parametro + "\".");
                    listaDeProcessos.add(new Processo(pidCounter, arquivoDoPrograma, prioridade, quantum, arrivalTime));
                    pidCounter++;
                } catch (IOException e) {
                    System.err.println("Erro ao carregar \"" + parametro + "\": " + e.getMessage());
                }
            }
            else
            {
                System.err.println("Erro ao carregar \"" + parametro + "\": arquivo indisponivel.");
            }
        }

        // Popula fila de pronto
        switch(this.politicaDoEscalonador)
        {
            case PRIORIDADE_COM_PREEMPCAO:
                listaDeProcessos.stream()
                    .filter(p -> p.getEstadoDoProcesso().equals(EstadoProcesso.READY) && p.getPrioridade() == 0)
                    .forEach(p -> {
                        if (!filaProcessosProntosAltaPrioridade.contains(p)) filaProcessosProntosAltaPrioridade.add(p);
                    });
                listaDeProcessos.stream()
                    .filter(p -> p.getEstadoDoProcesso().equals(EstadoProcesso.READY) && p.getPrioridade() == 1)
                    .forEach(p -> {
                        if (!filaProcessosProntosMediaPrioridade.contains(p)) filaProcessosProntosMediaPrioridade.add(p);
                    });
                listaDeProcessos.stream()
                    .filter(p -> p.getEstadoDoProcesso().equals(EstadoProcesso.READY) && p.getPrioridade() == 2)
                    .forEach(p -> {
                        if (!filaProcessosProntosBaixaPrioridade.contains(p)) filaProcessosProntosBaixaPrioridade.add(p);
                    });
                break;
            case ROUND_ROBIN:
                listaDeProcessos.stream()
                    .filter(p -> p.getEstadoDoProcesso().equals(EstadoProcesso.READY))
                    .forEach(p -> {
                        if (!filaProcessosProntosRR.contains(p)) filaProcessosProntosRR.add(p);
                    });
                break;
        }
    }

    public void escalonador()
    {
        imprimeEstado();
        switch(this.politicaDoEscalonador)
        {
            case PRIORIDADE_COM_PREEMPCAO:
                escalonadorPP();
                break;
            case ROUND_ROBIN:
                escalonadorRR();
                break;
            default:
                break;
        }
        imprimeEstadoFinal();        
        System.out.println("================ FIM DO OS =================");
        return;
    }

    private void escalonadorPP() {
        do {
            Processo processoAtual = consultaProcessoPP();
            if(processoAtual != null)
            {
                // DEBUG
                if (OS.verbose) System.out.println("> NOVO PROCESSO EM EXECUCAO: " + processoAtual);
                do {
                    atualizaBlockTime();
                    processoAtual.setEstadoDoProcesso(EstadoProcesso.RUNNING);
                    atualizaTempos();
                    processoAtual.setEstadoDoProcesso(processoAtual.processaLinha(politicaDoEscalonador));
                    imprimeEstado();
                    // PREEMPTACAO
                    if (!isMaiorPrioridade(processoAtual) && 
                        processoAtual.getEstadoDoProcesso() != EstadoProcesso.BLOCKED) 
                    {
                        processoAtual.setEstadoDoProcesso(EstadoProcesso.READY);
                        insereProcessoNaFilaDePronto(processoAtual);
                    }
                } while (processoAtual.getEstadoDoProcesso() == EstadoProcesso.RUNNING);
            } 
            else
            {
                atualizaBlockTime();
                atualizaTempos();
                imprimeEstado();
            }
        } while (!isTodosProcessosEncerrados());
    }

    private void escalonadorRR() {
        do {
            Processo processoAtual = consultaProcessoRR();
            if(processoAtual != null)
            {
                processoAtual.resetTimeout();
                // DEBUG
                if (OS.verbose) System.out.println("=> NOVO PROCESSO EM EXECUÇÃO: " + processoAtual);
                do {
                    atualizaBlockTime();
                    processoAtual.setEstadoDoProcesso(EstadoProcesso.RUNNING);
                    atualizaTempos();
                    processoAtual.setEstadoDoProcesso(processoAtual.processaLinha(politicaDoEscalonador));
                    imprimeEstado();
                    if (processoAtual.getEstadoDoProcesso() == EstadoProcesso.READY) insereProcessoNaFilaDePronto(processoAtual);
                } while (processoAtual.getEstadoDoProcesso() == EstadoProcesso.RUNNING);
            }
            else
            {
                atualizaBlockTime();
                atualizaTempos();
                imprimeEstado();
            }
        } while (!isTodosProcessosEncerrados());
    }


    private boolean isMaiorPrioridade(Processo processoAtual)
    {
        switch(processoAtual.getPrioridade())
        {
            case 0:
                return true;
            case 1:
                return filaProcessosProntosAltaPrioridade.isEmpty();
            case 2:
                return (filaProcessosProntosAltaPrioridade.isEmpty() && 
                    filaProcessosProntosMediaPrioridade.isEmpty());
        }
        return false;
    }

    private void atualizaBlockTime() {
        listaDeProcessos.stream()
            .filter(p -> p.getEstadoDoProcesso().equals(EstadoProcesso.BLOCKED))
            .forEach(p -> {
                p.decrementaBlockTime();
                if (p.getEstadoDoProcesso() == EstadoProcesso.READY) insereProcessoNaFilaDePronto(p);
            });
    }

    private void atualizaTempos() {
        passoDeExecucao++;
        listaDeProcessos.stream()
            .filter(p -> !p.getEstadoDoProcesso().equals(EstadoProcesso.EXIT))
            .forEach(p -> {
                if (p.computaTempoDoOS(passoDeExecucao).equals(EstadoProcesso.READY)) insereProcessoNaFilaDePronto(p);
            });
    }

    private void imprimeEstado()
    {
        System.out.println("=========== ESTADO DOS PROCESSOS =========== PASSO DE EXECUCAO DO OS = " + passoDeExecucao);
        listaDeProcessos.forEach(System.out::println);
        System.out.println("============================================");
        // DEBUG
        if (OS.verbose) imprimeFilaDeProntos();
    }

    private void imprimeEstadoFinal()
    {
        System.out.println("============ ESTADO FINAL DO OS ============ ULTIMO PASSO DE EXECUCAO DO OS = " + passoDeExecucao);
        listaDeProcessos.forEach(p -> System.out.println(p.toString() + "\n" + p.tempoDeEstadoString()));
    }

    private void imprimeFilaDeProntos()
    {
        System.out.println("============= FILA DE PRONTOS ==============");
        switch(this.politicaDoEscalonador)
        {
            case PRIORIDADE_COM_PREEMPCAO:
                filaProcessosProntosAltaPrioridade.forEach(System.out::println);
                filaProcessosProntosMediaPrioridade.forEach(System.out::println);
                filaProcessosProntosBaixaPrioridade.forEach(System.out::println);
                break;
            case ROUND_ROBIN:
                filaProcessosProntosRR.forEach(System.out::println);
                break;
            default:
                break;
        }
        System.out.println("============================================");
    }

    private boolean isTodosProcessosEncerrados ()
    {
        return listaDeProcessos.stream()
            .map(p -> p.getEstadoDoProcesso())
            .allMatch(ep -> ep.equals(EstadoProcesso.EXIT));
    }

    private Processo consultaProcessoPP()
    {
        if(!filaProcessosProntosAltaPrioridade.isEmpty())
            return filaProcessosProntosAltaPrioridade.poll();
        if(!filaProcessosProntosMediaPrioridade.isEmpty())
            return filaProcessosProntosMediaPrioridade.poll();
        if(!filaProcessosProntosBaixaPrioridade.isEmpty())
            return filaProcessosProntosBaixaPrioridade.poll();
        return null;
    }

    private Processo consultaProcessoRR()
    {
        return filaProcessosProntosRR.poll();
    }

    public void insereProcessoNaFilaDePronto(Processo processoAtual)
    {
        if (!processoAtual.getEstadoDoProcesso().equals(EstadoProcesso.READY)) 
            return;
        switch (politicaDoEscalonador) {
            case PRIORIDADE_COM_PREEMPCAO:
                switch(processoAtual.getPrioridade())
                {
                    case 0:
                        if (!filaProcessosProntosAltaPrioridade.contains(processoAtual))
                            filaProcessosProntosAltaPrioridade.add(processoAtual);
                        break;
                    case 1:
                        if (!filaProcessosProntosMediaPrioridade.contains(processoAtual))
                            filaProcessosProntosMediaPrioridade.add(processoAtual);
                        break;
                    case 2:
                        if (!filaProcessosProntosBaixaPrioridade.contains(processoAtual))
                            filaProcessosProntosBaixaPrioridade.add(processoAtual);
                        break;
                }
                break;
            case ROUND_ROBIN:
                if (!filaProcessosProntosRR.contains(processoAtual))
                    filaProcessosProntosRR.add(processoAtual);
                break;
            default:
                break;
        }
    }
}