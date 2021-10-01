import java.nio.file.Path;

public class Processo extends Primitivas {
    private Programa programa;
    private EstadoProcesso estado;
    private long pid;
    private int acc = 0;
    private int pc = 0;
    private long passoDeExecucaoDoOS;
    private int prioridade;
    private int quantum;
    
    public Processo(long pid, Path arquivoDoPrograma, long passoDeExecucaoDoOS, int prioridade, int quantum) throws Exception {
        this.pid = pid;
        this.programa = new Programa(arquivoDoPrograma);
        this.passoDeExecucaoDoOS = passoDeExecucaoDoOS;
        this.estado = EstadoProcesso.READY;
        this.prioridade = prioridade;
        this.quantum = quantum;

    }

    
    public void run ()
    {
        while (estado.equals(EstadoProcesso.RUNNING))
        {

            for (String linha : programa.code) {
                processaLinha(linha);
            }
        }
    }

    public void processaLinha (String linha)
    {
        String[] linhaDeComando = linha.toUpperCase().split(" ");
        String complemento = linhaDeComando[1];
        int labelAddress = 0;
        int op1 = 0;
        if (Character.isLetter(complemento.charAt(0)))
        {
            labelAddress = programa.labels.get(complemento);
        }
        else
        {
            op1 = complemento.startsWith("#") ? 
                Integer.valueOf(complemento.substring(1)) :
                Integer.valueOf(programa.data.get(complemento));
        }
        switch (linhaDeComando[0])
        {
            case "ADD":
                aritimeticoAdd(this.acc, op1);
                break;
                case "SUB":
                aritimeticoSub(this.acc, op1);
                break;
            case "MULT":
                aritimeticoMult(this.acc, op1);
                break;
            case "DIV":
                aritimeticoDiv(this.acc, op1);
                break;
            case "LOAD":
                memoriaLoad(this.acc, op1);
                break;
            case "STORE":
                if (complemento.startsWith("#"))
                    memoriaStore(this.acc, op1);
                else
                    memoriaStore(this.acc, op1);
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
        return "Processo [acc=" + acc + ", estado=" + estado + ", passoDeExecucaoDoOS=" + passoDeExecucaoDoOS + ", pc="
                + pc + ", pid=" + pid + ", prioridade=" + prioridade + ", programa=" + programa + ", quantum=" + quantum
                + "]";
    }

    
}
