public class Startup {
    
    // Modo de teste
    public static boolean teste = true;
    public static final String appsDirectory = "apps";
    public static void main (String[] args)
    {
        if (Startup.teste) {
            String[] argsTeste = {"oi1", "-1l", "-oi2", "-p", "pp", "1", "-l", "d", "Prog1.txt", "1", "-g"};
            Kernel testeOs = new Kernel(argsTeste);
            return;
        }

        Kernel polvoOs;
        // Tratar exceções do construtor
        try {
            polvoOs = new Kernel(args);
        } catch (Exception e) {
            // exibir help
        }

        // Bloco de execução
        try {
            //polvoOs.run();
        } catch (Exception e) {
            // exibir erros de execução
        }
    }
}
