import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Programa extends Primitivas
{
    private int acc = 0;
    private int pc = 0;
    private List<String> code = new ArrayList<>();
    private Map<String, Integer> data = new HashMap<>();
    private Map<String, Integer> lables = new HashMap<>();
    
    public Programa(String nomeDoArquivo)
    {
        
    }

    public void run ()
    {

    }

    public void processaLinha ()
    {
        
    }




    /*
    public int aritimeticoAdd (int acc, int valor)
    {
        return acc + valor;
    }
    public int aritimericoAdd (int acc, Integer op1Ref)
    {
        return aritimeticoAdd (acc, op1Ref.intValue());
    }
    public int aritimeticoSub (int acc, int valor)
    {
        return acc - valor;
    }
    public int aritimericoSub (int acc, Integer op1Ref)
    {
        return aritimeticoSub (acc, op1Ref.intValue());
    }
    public int aritimeticoMult (int acc, int valor)
    {
        return acc * valor;
    }
    public int aritimericoMult (int acc, Integer op1Ref)
    {
        return aritimeticoMult (acc, op1Ref.intValue());
    }
    public int aritimeticoDiv (int acc, int valor)
    {
        return acc / valor;
    }
    public int aritimericoDiv (int acc, Integer op1Ref)
    {
        return aritimeticoDiv (acc, op1Ref.intValue());
    }
    public void memoriaLoad (Integer accRef, int op1)
    {
        accRef = op1;
    }
    public void memoriaLoad (Integer accRef, Integer op1Ref)
    {
        memoriaLoad(accRef, op1Ref.intValue());
    }
    public void memoriaStore (Integer op1Ref, Integer accRef)
    {
        op1Ref = accRef;
    }
    public int saltoBRANY (Integer pc, int labelAddress)
    {
        pc = labelAddress;
    }
    */
}
