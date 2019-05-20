package Logic;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author Dominik
 */
public class Tests {

    /**
     * Konštruktor
     */
    public Tests() {
    }

    /**
     * Otestuje heuristiky
     *
     * @param numberValue počet jedinečných hodnôt funkcie
     * @param numberVariable počet premennýc funkcie
     * @param numberFunction počet testovaných funkcií
     */
    public boolean testFunction(int numberValue, int numberVariable, int numberFunction) throws IOException {
        Algorithms a = new Algorithms();
        String pom;
        MVFunction function;
        MVTable table;
        int sizeFunction = (int) Math.pow(numberValue, numberVariable);
        double allEntropyResult = 0.0;
        double firstEntropyResult = 0.0;
        double derivate = 0.0;
        int read = 0;
        String resultString = "";

        String[][] entropy;
        String[] deriv;
        ArrayList<String> result;
        TreeMap<String, String> allfunction = new TreeMap<>();
        if (numberValue == 3 && numberVariable == 2) {
            ArrayList<String> pomList = new ArrayList<>();
            char[] poMChar = {'0', '1', '2'};
            a.printAllKlenghtComb(poMChar, sizeFunction, pomList);
            int pomc = 0;
            for (int i = 0; i < pomList.size(); i++) {
                pom = pomList.get(i);
                read++;
                function = new MVFunction(pom, numberValue);
                table = new MVTable(function);

                entropy = a.entropy(table, function.getArrayUniqNumberFunctionChar());
                deriv = a.getBestPathFromDerivation(a.logicDerivation(table), function);

                allEntropyResult += (double) (Integer.parseInt(entropy[0][0]));
                firstEntropyResult += (double) (Integer.parseInt(entropy[1][0]));
                derivate += (double) (Integer.parseInt(deriv[0]));
            }
            allEntropyResult = allEntropyResult / pomList.size();
            firstEntropyResult = firstEntropyResult / pomList.size();
            derivate = derivate / pomList.size();
        } else {
            int size = numberFunction;
            int sizeAllFunction = (int) Math.pow(Math.pow(numberValue, numberValue), numberVariable);
            if (size > sizeAllFunction) {
                size = sizeAllFunction;
            }
            for (int k = 1; k <= size; k++) {
                read++;
                pom = generateStringFunc(sizeFunction, numberValue);
                if (numberVariable <= 5) {
                    while (allfunction.containsKey(pom)) {
                        pom = generateStringFunc(sizeFunction, numberValue);
                    }
                }
                allfunction.put(pom, pom);

                function = new MVFunction(pom, numberValue);
                table = new MVTable(function);
                entropy = a.entropy(table, function.getArrayUniqNumberFunctionChar());
                deriv = a.getBestPathFromDerivation(a.logicDerivation(table), function);

                allEntropyResult += (double) (Integer.parseInt(entropy[0][0]));
                firstEntropyResult += (double) (Integer.parseInt(entropy[1][0]));
                derivate += (double) (Integer.parseInt(deriv[0]));
            }
            allEntropyResult = allEntropyResult / size;
            firstEntropyResult = firstEntropyResult / size;
            derivate = derivate / size;
        }
        resultString += "Vysledné hodnoty pre počet hodnôt funckie: " + numberValue + " a pre počet premenných: " + numberVariable + System.getProperty("line.separator");
        resultString += "Počet funkcii: " + read + System.getProperty("line.separator");
        resultString += "Entropia  za podmienky všetkých premenných: " + allEntropyResult + System.getProperty("line.separator");
        resultString += "Entropia  za podmienky jednej premennej: " + firstEntropyResult + System.getProperty("line.separator");
        resultString += "Logické derivácie: " + derivate + System.getProperty("line.separator");
        String nameFile = "testy/" + numberValue + "," + numberVariable;
        saveResultToFile(nameFile, resultString);
        return true;
    }

    /**
     * Vygeneruje retazec funkcie
     *
     * @param sizeFunction dĺžka funkcie
     * @param countUniqNumb počet jedinečných hodnôt funkcie
     * @return vráti retazec funkcie
     */
    public String generateStringFunc(int sizeFunction, int countUniqNumb) {
        int x;
        String genRes = "";
        ArrayList<String> arr = new ArrayList<>();
        int pomCount = 0;
        for (int i = 0; i < sizeFunction; i++) {
            x = (int) (Math.random() * countUniqNumb);
            pomCount++;
            if (pomCount == 100000) {
                pomCount = 0;
                arr.add(genRes);
                genRes = "";
            }
            genRes += (char) (x + 48);
        }
        arr.add(genRes);
        genRes = "";
        for (int i = 0; i < arr.size(); i++) {
            genRes += arr.get(i);
        }
        return genRes;
    }

    /**
     * Zapíše do súboru výsledky testov
     *
     * @param fileName nazov súboru
     * @param text vysledok testov
     */
    public void saveResultToFile(String fileName, String text) throws FileNotFoundException, IOException {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName + ".txt"), "utf-8"));
            writer.write(text);           
        } catch (IOException ex) {
            System.out.println("Nepodarilo sa zapisat do suboru");
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }
    }
}
