package Logic;

/**
 *
 * @author Dominik
 */
public class MVFunction {

    private char[] arrayOfCharNumbFunc;
    private String functionString;
    int numberOfVariable, sizeOfUniqNumberInFunc;

    /**
     * Konštruktor
     *
     * @param function reťazec funckie
     * @param sizeOfUniqNumber počet jedinečných hodnôt funckie
     */
    public MVFunction(String function, int sizeOfUniqNumber) {
        functionString = function;
        sizeOfUniqNumberInFunc = sizeOfUniqNumber;
        numberOfVariable = degree(sizeOfUniqNumberInFunc, function);
        createArrayOfUniqNUmbChar();
    }

    /**
     * Vráti retazec funkcie
     *
     * @return vráti ratazec
     */
    public String getFunctionString() {
        return functionString;
    }

    /**
     * Na základe jedinečných hodnot a počtu premenných vypočíta dĺžku funkcie
     *
     * @return vráti dĺžku funkcie
     */
    public int sizeCompleteFunction() {
        return (int) Math.pow(sizeOfUniqNumberInFunc, numberOfVariable);
    }

    /**
     * Getter pre dlžku retazca funkcie
     *
     * @return vráti dĺžku funkcie
     */
    public int sizeFunction() {
        return functionString.length();
    }

    /**
     * Getter pre jedinečné hodnoty funkcie
     *
     * @return vráti jedinečné hodnoty funkcie
     */
    public int getSizeOfUniqNumberInFunc() {
        return sizeOfUniqNumberInFunc;
    }

    /**
     * Getter pre počet premenných funkcie
     *
     * @return počet premenných funkcie
     */
    public int getNumberOfVariable() {
        return numberOfVariable;
    }

    /**
     * Vypočíta počet premenných funkcie
     *
     * @param x počet jedinečných hodnôt funkcie
     * @param stringNumber reťazec premennej
     * @return vráti počet premenných
     */
    public int degree(int x, String stringNumber) {
        if (sizeOfUniqNumberInFunc > 1) {
            int n = 1;
            while (Math.pow(x, n) < stringNumber.length()) {
                n++;
            }
            return n;
        } else {
            return 0;
        }
    }

    /**
     * Naplní pole všetkých možných jedinečných hodnôt funkcie
     */
    public void createArrayOfUniqNUmbChar() {
        arrayOfCharNumbFunc = new char[sizeOfUniqNumberInFunc];
        for (int i = 0; i < sizeOfUniqNumberInFunc; i++) {
            arrayOfCharNumbFunc[i] = (char) (i + 48);
        }
    }

    /**
     * Getter pre pole všetkých možných jedinečných hodnôt funkcie
     *
     * @return vráti pole
     */
    public char[] getArrayUniqNumberFunctionChar() {
        return arrayOfCharNumbFunc;
    }

    /**
     * Skontroluje správnosť funkcie
     *
     * @return vráti true ak je správna inak false
     */
    public boolean checkFunction() {
        if (functionString.length() == sizeCompleteFunction() && sizeOfUniqNumberInFunc > 1) {          
            for (char ch : functionString.toCharArray()) {
                if (((int) ch) - 48 >= sizeOfUniqNumberInFunc) {
                    return false;
                }
            }
        } else {           
            return false;
        }
        return true;
    }
}
