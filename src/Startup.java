public class Startup {
    
    // Modo de teste
    public static boolean teste = false;
    public static final String appsDirectory = "/../apps/";
    public static void main (String[] args)
    {
        if (args.length == 0)
        {
            Kernel.imprimeHelp();
            return;
        }
        if (args[0].equals("-t")) {
            teste = true;
            String[] argsTeste = {"oi1", "-1l", "-oi2", "-p", "rr", "10", "-l", "d", "Prog2.txt", "0", "-g"};
            Kernel testeOs = new Kernel(argsTeste);
            testeOs.run();
            return;
        }

        Kernel polvoOs = null;
        // Tratar exceções do construtor
        try {
            polvoOs = new Kernel(args);
        } catch (Exception e) {
            // exibir help
            System.out.println(e.getStackTrace().toString());
        }

        // Bloco de execução
        try {
            polvoOs.run();
        } catch (RuntimeException e) {
            // exibir erros de execução
            System.out.println(e.getStackTrace().toString());
        }
    }
}
