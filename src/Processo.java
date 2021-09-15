public class Processo {
    private Programa programa;
    private int ultimoPassoDoOS;
    private EstadoProcesso estado;
    // TODO: definir como popular um dos parâmetros
    private int prioridade;
    private int quantum;
    
    public Processo(Programa programa, int ultimoPassoDoOS, EstadoProcesso estado) {
        this.programa = programa;
        this.ultimoPassoDoOS = ultimoPassoDoOS;
        this.estado = estado;
    }

    public Programa getPrograma() {
        return programa;
    }

    public int getUltimoPassoDoOS() {
        return ultimoPassoDoOS;
    }

    public void setUltimoPassoDoOS(int ultimoPassoDoOS) {
        this.ultimoPassoDoOS = ultimoPassoDoOS;
    }

    public EstadoProcesso getEstadoDoProcesso() {
        return estado;
    }

    public void setEstadoDoProcesso(EstadoProcesso novoEstado) {
        this.estado = novoEstado;
    }


}
