public class Startup {
    public static void main (String[] args)
    {
        String[] teste = {"teste1", "teste2"};
        Kernel testeOs = new Kernel(teste);
        
        
        Kernel polvoOs = new Kernel(args);
        polvoOs.run();
    }
}
