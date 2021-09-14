public class Primitivas {
    public int aritimeticoAdd (int acc, int valor)
    {
        return acc + valor;
    }
    public int aritimericoAdd (int acc, Integer ref)
    {
        return aritimeticoAdd (acc, ref.intValue());
    }
    public int aritimeticoSub (int acc, int valor)
    {
        return acc - valor;
    }
    public int aritimericoSub (int acc, Integer ref)
    {
        return aritimeticoSub (acc, ref.intValue());
    }
    public int aritimeticoMult (int acc, int valor)
    {
        return acc * valor;
    }
    public int aritimericoMult (int acc, Integer ref)
    {
        return aritimeticoMult (acc, ref.intValue());
    }
    public int aritimeticoDiv (int acc, int valor)
    {
        return acc / valor;
    }
    public int aritimericoDiv (int acc, Integer ref)
    {
        return aritimeticoDiv (acc, ref.intValue());
    }


}
