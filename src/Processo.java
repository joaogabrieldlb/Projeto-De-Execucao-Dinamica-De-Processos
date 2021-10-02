import java.nio.file.Path;

public class Processo extends Primitivas {
    private Programa programa;
    private String nomeDoPrograma;
    private EstadoProcesso estado;
    private long pid;
    private int acc = 0;
    private int pc = 0;
    private long passoDeExecucaoDoOS;
    private int prioridade;
    private int quantum;
    
    public Processo(long pid, Path arquivoDoPrograma, long passoDeExecucaoDoOS, int prioridade, int quantum) throws Exception {
        this.pid = pid;
        this.nomeDoPrograma = arquivoDoPrograma.getFileName().toString();
        this.programa = new Programa(arquivoDoPrograma);
        this.passoDeExecucaoDoOS = passoDeExecucaoDoOS;
        this.estado = EstadoProcesso.READY;
        this.prioridade = prioridade;
        this.quantum = quantum;

    }

    public EstadoProcesso processaLinha ()
    {
        String linha = programa.code.get(this.pc);
        // DEBUG
        if (Startup.teste) System.out.println("LINHA DE EXECUÇÃO: " + linha);

        String[] linhaDeComando = linha.toUpperCase().split(" ");
        String complemento = linhaDeComando[1];
        Integer labelAddress = 0;
        int op1 = 0;
        labelAddress = programa.labels.get(complemento);
        op1 = complemento.startsWith("#") ? 
            Integer.valueOf(complemento.substring(1)) :
            Integer.valueOf(programa.data.get(complemento));
        switch (linhaDeComando[0])
        {
            case "ADD":
                this.acc = aritimeticoAdd(this.acc, op1);
                this.pc++;
                break;
            case "SUB":
                aritimeticoSub(this.acc, op1);
                this.pc++;
                break;
            case "MULT":
                aritimeticoMult(this.acc, op1);
                this.pc++;
                break;
            case "DIV":
                aritimeticoDiv(this.acc, op1);
                this.pc++;
                break;
            case "LOAD":
                this.acc = memoriaLoad(this.acc, op1);
                this.pc++;
                break;
            case "STORE":
                if (complemento.startsWith("#"))
                    throw new RuntimeException("Operação SOTRE com complemento invalido");
                else
                    memoriaStore(this.acc, op1);
                this.pc++;
                break;
            case "BRANY":
                saltoBRANY(this.pc, labelAddress);
                break;
            case "BRPOS":
                saltoBRPOS(this.pc, labelAddress, this.acc);
                break;
            case "BRZERO":
                saltoBRZERO(this.pc, labelAddress, this.acc);
                break;
            case "BRNEG":
                saltoBRNEG(this.pc, labelAddress, this.acc);
                break;
            case "SYSCALL":
                break;
            default:
                throw new RuntimeException("Linha de programa invalida.");
        }
        return this.getEstadoDoProcesso();
    }

    @Override
    public void sistemaSYSCALL(int index) {
        // TODO Auto-generated method stub
        
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
            + ", quantum=" + quantum 
            + ", pc=" + pc
            + ", acc=" + acc
            + ", passoDeExecucaoDoOS=" + passoDeExecucaoDoOS
            + "]";
    }
}
