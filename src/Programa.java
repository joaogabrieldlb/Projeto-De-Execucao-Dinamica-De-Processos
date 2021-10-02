import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Programa
{
    static String[] markers = { ".code", ".endcode", ".data", ".enddata" };
    protected List<String> code = new ArrayList<>();
    protected Map<String, String> data = new HashMap<>();
    protected Map<String, Integer> labels = new HashMap<>();
    
    public Programa(Path arquivoDoPrograma) throws Exception
    {
        List<String> codigoFonte = Files.readAllLines(arquivoDoPrograma, StandardCharsets.UTF_8);
        int codeStart = codigoFonte.indexOf(markers[0]);
        int codeEnd = codigoFonte.indexOf(markers[1]);
        int dataStart = codigoFonte.indexOf(markers[2]);
        int dataEnd = codigoFonte.indexOf(markers[3]);
        boolean checkCode = codeStart == -1 || codeEnd == -1 || (codeEnd - codeStart) <= 1;
        boolean checkData = dataStart == -1 ^ dataEnd == -1 || (dataEnd - dataStart) < 0;
        if (checkCode || checkData)
        {
            throw new InvalidPropertiesFormatException("Arquivo com estrutura invalida.");
        }

        codigoFonte.subList(codeStart + 1, codeEnd).forEach(s -> code.add(s.trim().toUpperCase()));

        for (String string : code) {
            if (string.endsWith(":"))
            {
                labels.put(string, code.indexOf(string) - labels.size());
            }
        }

        code.removeIf(s -> s.endsWith(":"));

        if ((dataEnd - dataStart) > 1) {
            data = codigoFonte.subList(dataStart + 1, dataEnd).stream().map(s -> s.trim().toUpperCase().split(" ")).collect(Collectors.toMap(s -> s[0], s -> s[1]));
        }
    }
}
