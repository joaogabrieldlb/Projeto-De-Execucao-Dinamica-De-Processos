public class Startup {
    
    // Modo de teste
    public static boolean teste = false;
    // public static final String appsDirectory = "./../apps/";
    public static final String appsDirectory = "apps";
    public static void main(String[] args)
    {
        // if (args.length == 0)
        // {
        //     imprimeHelp();
        //     return;
        // }
        // if (args[0].toLowerCase().equals("-t")) 
        {
            teste = true;
            String[] argsTeste = {"oi1", "-1l", "-oi2", "-p", "pp", "10", "-l", "d", "teste.txt", "0", "Prog1.txt", "1", "Prog1.txt", "0","Prog2.txt", "0", "Prog3.txt", "2", "-g"};
            Kernel testeOs = new Kernel(argsTeste);
            testeOs.escalonador();
            return;
        }

        // Kernel polvoOs = null;
        // // Tratar exceções do construtor
        //     polvoOs = new Kernel(args);
        //     polvoOs.escalonador();
        // try {
        // } catch (Exception e) {
        //     // exibir help
        //     System.out.println(e.getStackTrace().toString());
        // }

        // // Bloco de execução
        // try {
        // } catch (RuntimeException e) {
        //     // exibir erros de execução
        //     System.out.println(e.getStackTrace().toString());
        // }
    }
    
	public static void imprimeHelp()
    {
        System.out.println("Uso do OS:");
        System.out.println("Modo de teste: \"java Startup -t\"");
        System.out.println("Modo normal: \"java Startup -p [politica | quantum] -l [lista de programas | prioridade]\"");
	}
}
