public abstract class Primitivas {
    public void aritimeticoAdd (int acc, int op1)
    {
        acc += op1;
    }
    public void aritimeticoSub (int acc, int op1)
    {
        acc -= op1;
    }
    public void aritimeticoMult (int acc, int op1)
    {
        acc *= op1;
    }
    public void aritimeticoDiv (int acc, int op1)
    {
        acc /= op1;
    }
    public void memoriaLoad (int acc, int op1)
    {
        acc = op1;
    }
    public void memoriaStore (int op1, int acc)
    {
        op1 = acc;
    }
    public void saltoBRANY (int pc, int labelAddress)
    {
        pc = labelAddress;
    }
    public void saltoBRPOS (int pc, int labelAddress, int acc)
    {
        if (acc > 0) pc = labelAddress;
    }
    public void saltoBRZERO (int pc, int labelAddress, int acc)
    {
        if (acc == 0) pc = labelAddress;
    }
    public void saltoBRNEG (int pc, int labelAddress, int acc)
    {
        if (acc < 0) pc = labelAddress;
    }
    public void sistemaSYSCALL (int index)
    {
        // TODO:    
    }
}
