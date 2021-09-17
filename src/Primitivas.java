public abstract class Primitivas {
    public void aritimeticoAdd (Integer acc, int valor)
    {
        acc += valor;
    }
    public void aritimericoAdd (Integer acc, Integer op1Ref)
    {
        aritimeticoAdd (acc, op1Ref.intValue());
    }
    public void aritimeticoSub (Integer acc, int valor)
    {
        acc -= valor;
    }
    public void aritimericoSub (Integer acc, Integer op1Ref)
    {
        aritimeticoSub (acc, op1Ref.intValue());
    }
    public void aritimeticoMult (Integer acc, int valor)
    {
        acc *= valor;
    }
    public void aritimericoMult (Integer acc, Integer op1Ref)
    {
        aritimeticoMult (acc, op1Ref.intValue());
    }
    public void aritimeticoDiv (Integer acc, int valor)
    {
        acc /= valor;
    }
    public void aritimericoDiv (Integer acc, Integer op1Ref)
    {
        aritimeticoDiv (acc, op1Ref.intValue());
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
    public void saltoBRANY (Integer pc, int labelAddress)
    {
        pc = labelAddress;
    }
    public void saltoBRPOS (Integer pc, int labelAddress, Integer acc)
    {
        if (acc.intValue() > 0) pc = labelAddress;
    }
    public void saltoBRZERO (Integer pc, int labelAddress, Integer acc)
    {
        if (acc.intValue() == 0) pc = labelAddress;
    }
    public void saltoBRNEG (Integer pc, int labelAddress, Integer acc)
    {
        if (acc.intValue() < 0) pc = labelAddress;
    }
    public void sistemaSYSCALL (int index)
    {
        // TODO:    
    }
}
