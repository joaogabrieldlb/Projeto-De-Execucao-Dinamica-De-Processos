import java.nio.file.Path;
import java.util.Random;
import java.util.Scanner;

public class Processo extends Primitivas {
    private Scanner in = new Scanner(System.in);
    private Programa programa;
    private String nomeDoPrograma;
    private EstadoProcesso estado;
    private long pid;
    private int acc = 0;
    private int pc = 0;
    private long passoDeExecucaoDoOS;
    private int prioridade;
    private int timeout;
    private int blockTime;
    
    public Processo(long pid, Path arquivoDoPrograma, long passoDeExecucaoDoOS, int prioridade, int quantum) throws Exception {
        this.pid = pid;
        this.nomeDoPrograma = arquivoDoPrograma.getFileName().toString();
        this.programa = new Programa(arquivoDoPrograma);
        this.passoDeExecucaoDoOS = passoDeExecucaoDoOS;
        this.estado = EstadoProcesso.READY;
        this.prioridade = prioridade;
        this.timeout = quantum;
    }

    public EstadoProcesso processaLinha (Kernel.PoliticaDeEscalonamento politica)
    {
        this.estado = EstadoProcesso.RUNNING;
        String linha = programa.code.get(this.pc);
        // DEBUG
        if (Startup.teste) System.out.println("LINHA DE EXECUÇÃO: " + linha);

        String[] linhaDeComando = linha.toUpperCase().split(" ");
        String complemento = linhaDeComando[1];
        Integer labelAddress = 0;
        Integer op1 = 0;
        labelAddress = programa.labels.get(complemento);
        try {
            op1 = complemento.startsWith("#") ? 
                Integer.valueOf(complemento.substring(1)) :
                Integer.valueOf(programa.data.get(complemento));
        } catch(Exception e)
        {}
        switch (linhaDeComando[0])
        {
            case "ADD":
                this.acc = aritimeticoAdd(this.acc, op1);
                this.pc++;
                break;
            case "SUB":
                this.acc = aritimeticoSub(this.acc, op1);
                this.pc++;
                break;
            case "MULT":
                this.acc = aritimeticoMult(this.acc, op1);
                this.pc++;
                break;
            case "DIV":
                this.acc = aritimeticoDiv(this.acc, op1);
                this.pc++;
                break;
            case "LOAD":
                this.acc = memoriaLoad(op1);
                this.pc++;
                break;
            case "STORE":
                if (complemento.startsWith("#"))
                    throw new RuntimeException("Operação SOTRE com complemento invalido");
                else
                    this.programa.data.put(complemento, memoriaStore(this.acc));
                this.pc++;
                break;
            case "BRANY":
                this.pc = saltoBRANY(labelAddress);
                break;
            case "BRPOS":
                this.pc = saltoBRPOS(this.pc, labelAddress, this.acc);
                break;
            case "BRZERO":
                this.pc = saltoBRZERO(this.pc, labelAddress, this.acc);
                break;
            case "BRNEG":
                this.pc = saltoBRNEG(this.pc, labelAddress, this.acc);
                break;
            case "SYSCALL":
                return sistemaSYSCALL(complemento);
            default:
                throw new RuntimeException("Linha de programa invalida.");
        }

        if (politica == Kernel.PoliticaDeEscalonamento.ROUND_ROBIN)
        {
            this.timeout--;
            if (this.timeout == 0) this.estado = EstadoProcesso.READY;
        }
        return this.getEstadoDoProcesso();
    }

    @Override
    public EstadoProcesso sistemaSYSCALL(String index) {
        switch(index)
        {
            case "0":
                return EstadoProcesso.EXIT;
            case "1":
                System.out.println("ACC = " + this.acc);
                return blockTime();
            case "2":
                while (true)
                {
                    try {
                        int value = Integer.parseInt(in.nextLine());
                        this.acc = value;
                        break;
                    } catch (Exception e) {
                        System.out.println("Valor deve ser inteiro.");
                    }
                }
                return blockTime();
            default:
                throw new RuntimeException("Linha de programa invalida.");
        }
    }

    public void setQuantum(int quantum) {
        this.timeout = quantum;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private EstadoProcesso blockTime()
    {
        Random aleatorio = new Random();
        this.blockTime = 10 + aleatorio.nextInt(11);
        // DEBUG
        if (Startup.teste) System.out.println("BLOCKED TIME: " + this.blockTime);

        this.pc++;
        return EstadoProcesso.BLOCKED;
    }

    public void decrementaBlockTime() {
        this.blockTime--;
        if (blockTime == 0) this.estado = EstadoProcesso.READY;
    }
    
    public int getPrioridade() {
        return prioridade;
    }

    public Programa getPrograma() {
        return programa;
    }

    public long getUltimoPassoDoOS() {
        return passoDeExecucaoDoOS;
    }

    public void setUltimoPassoDoOS(int ultimoPassoDoOS) {
        this.passoDeExecucaoDoOS = ultimoPassoDoOS;
    }

    public EstadoProcesso getEstadoDoProcesso() {
        return estado;
    }

    public void setEstadoDoProcesso(EstadoProcesso novoEstado) {
        this.estado = novoEstado;
    }

    @Override
    public String toString() {
        return "Processo ["
            + "pid=" + pid
            + ", nomeDoPrograma=" + nomeDoPrograma
            + ", estado=" + estado
            + ", prioridade=" + prioridade 
            + ", timeout=" + timeout
            + ", pc=" + pc
            + ", acc=" + acc
            + ", passoDeExecucaoDoOS=" + passoDeExecucaoDoOS
            + "]";
    }
}
