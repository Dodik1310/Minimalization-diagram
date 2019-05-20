package Logic;

/**
 *
 * @author Dominik
 */
public class MVTable {

    private char[] header;
    private int[][] table;
    private char[] headerDerivation;
    private int[][] tableDerivation;
    private MVFunction function;
    int resultDerivation;

    /**
     * Konštruktor
     *
     * @param paFunction reťazec funckie
     */
    public MVTable(MVFunction paFunction) {
        function = paFunction;
        resultDerivation = 0;
        createTable();
    }

    /**
     * Getter pre funkciu
     *
     * @return vráti funkciu
     */
    public MVFunction getFunction() {
        return function;
    }

    /**
     * Getter pre maticu funkcie
     *
     * @return vráti maticu
     */
    public int[][] getTable() {
        return table;
    }

    /**
     * Getter pre hlavičku
     *
     * @return vráti hlavičku
     */
    public char[] getHeadre() {
        return header;
    }

    /**
     * Getter pre hlavičku po derivácii
     *
     * @return vráti hlavičku po derivácii
     */
    public char[] getHeadreDerive() {
        return headerDerivation;
    }

    /**
     * Getter pre maticu po derivácii
     *
     * @return vráti maticu
     */
    public int[][] getTableDerivation() {
        return tableDerivation;
    }

    /**
     * Vytvorí tabuľku, ktorá reprezentuje funckiu
     */
    public void createTable() {
        String stringFunction = function.getFunctionString();
        table = new int[stringFunction.length()][function.getNumberOfVariable() + 1];
        header = new char[function.getNumberOfVariable() + 1];

        for (int i = 0; i < header.length; i++) {
            if (i == header.length - 1) {
                header[i] = '*';
            } else {
                header[i] = (char) (i + 97);
            }
        }
        int pomAll = (int) Math.pow(function.getSizeOfUniqNumberInFunc(), function.getNumberOfVariable());
        int pomSpace = (int) pomAll / function.getSizeOfUniqNumberInFunc();
        for (int j = 0; j < function.getNumberOfVariable() + 1; j++) {
            int actualNumberInTab = 0;
            int pomNumb = 0;
            for (int i = 0; i < stringFunction.length(); i++) {
                if (j == function.getNumberOfVariable()) {
                    table[i][j] = Integer.parseInt("" + stringFunction.charAt(i));
                } else {
                    table[i][j] = actualNumberInTab;
                    pomNumb++;
                    if (pomNumb == pomSpace) {
                        pomNumb = 0;
                        actualNumberInTab++;
                        if (actualNumberInTab == function.getSizeOfUniqNumberInFunc()) {
                            actualNumberInTab = 0;
                        }
                    }
                }
            }
            pomSpace /= function.getSizeOfUniqNumberInFunc();
        }
    }

    /**
     * Zderivuje funkciu podľa zadaných parametrov
     *
     * @param indexVariable premenná, podľa ktorej sa derivuje
     * @param fromVar začiatočný smer derivovania premennej
     * @param toVar koncový smer derivovania premennej
     * @param fromFunct začiatočný smer derivovania funkcie
     * @param toFunt koncový smer derivovania premennej
     * @return vráti tabuľku po derivácií
     */
    public int[][] derive(int indexVariable, int fromVar, int toVar, int fromFunct, int toFunt) {
        resultDerivation = 0;
        headerDerivation = new char[function.getNumberOfVariable()];
        int pomHead = 0;
        for (int i = 0; i < header.length; i++) {
            if (i != indexVariable) {
                headerDerivation[pomHead] = header[i];
                pomHead++;
            }
        }
        int sizeCompare = (int) Math.pow(function.getSizeOfUniqNumberInFunc(), function.getNumberOfVariable() - 1);
        tableDerivation = new int[sizeCompare][function.getNumberOfVariable()];
        int pom = (function.getNumberOfVariable() - 1) - indexVariable;
        int differentBettwenValue = (int) Math.pow(function.getSizeOfUniqNumberInFunc(), pom);
        int firstIndex = differentBettwenValue * fromVar;
        int secondIndex = differentBettwenValue * toVar;

        int result = 0;
        int pomIndexRow = 0, pomIndexColum = 0;
        int pomChange = 0;
        for (int i = 0; i < sizeCompare; i++) {
            pomChange++;
            if (table[firstIndex][indexVariable] == fromVar && table[firstIndex][function.getNumberOfVariable()] == fromFunct
                    && table[secondIndex][indexVariable] == toVar && table[secondIndex][function.getNumberOfVariable()] == toFunt) {
                result = 1;
                resultDerivation++;
            } else {
                result = 0;
            }

            for (int k = 0; k < table[0].length - 1; k++) {
                if (k != indexVariable) {
                    tableDerivation[pomIndexRow][pomIndexColum] = table[firstIndex][k];
                    pomIndexColum++;
                }
            }

            tableDerivation[pomIndexRow][pomIndexColum] = result;
            pomIndexRow++;
            pomIndexColum = 0;
            if (pomChange == differentBettwenValue) {
                firstIndex += ((differentBettwenValue * function.getSizeOfUniqNumberInFunc()) - (differentBettwenValue - 1));
                secondIndex += ((differentBettwenValue * function.getSizeOfUniqNumberInFunc()) - (differentBettwenValue - 1));
                pomChange = 0;
            } else {
                firstIndex++;
                secondIndex++;
            }
        }
        return tableDerivation;
    }

    /**
     * Vráti počet nenulových hodnot po derivácií z posledného stĺpca matice
     *
     * @return vráti výslednú hodnotu
     */
    public int getResultDerivationFromTable() {
        int result = 0;
        if (tableDerivation != null) {
            for (int i = 0; i < tableDerivation.length; i++) {
                if (tableDerivation[i][tableDerivation[0].length - 1] == 1) {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * Getter pre počet nenulových hodnot po derivácií
     *
     * @return vráti výslednú hodnotu
     */
    public int getResultDerivation() {
        return resultDerivation;
    }

    /**
     * Vzpíše zadaná maticu
     *
     * @param tab matica na vypísanie
     */
    public void printMatrix(int[][] tab) {
        if (tab != null) {
            for (int j = 0; j < tab.length; j++) {
                for (int i = 0; i < tab[0].length; i++) {
                    if (i < tab[0].length - 1) {
                        System.out.print(tab[j][i]);
                    } else {
                        System.out.print("|" + tab[j][i]);
                    }
                }
                System.out.println();
            }
        }
    }
}
