import java.nio.file.Path;

public class Processo {
    private Programa programa;
    private long passoDeExecucaoDoOS;
    private EstadoProcesso estado;
    private int prioridade;
    private int quantum;
    
    public Processo(Path arquivoDoPrograma, long passoDeExecucaoDoOS, int prioridade, int quantum) {
        this.programa = new Programa(arquivoDoPrograma);
        this.passoDeExecucaoDoOS = passoDeExecucaoDoOS;
        this.estado = EstadoProcesso.READY;
        this.prioridade = prioridade;
        this.quantum = quantum;

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


}
