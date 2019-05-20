package Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author Dominik
 */
public class Algorithms {

    ArrayList<String> combinationString;
    ArrayList<String> combinationSameLeng;

    /**
     * Konštruktor, ktorý vytvorí zoznamy pre algoritmy
     */
    public Algorithms() {
        combinationString = new ArrayList<String>();
        combinationSameLeng = new ArrayList<String>();
    }

    /**
     * Getter pre kombinácie určitej dlžky
     *
     * @return vrati kombinácie
     */
    public ArrayList<String> getCombinationSameLeng() {
        return combinationSameLeng;
    }

    /**
     * Getter pre všetky kombinácie bez opakovania
     *
     * @return vráti kombinácie
     */
    public ArrayList<String> getCombinationString() {
        return combinationString;
    }

    /**
     * Urobí exaktný algoritmus, ktorý vyskúša vytvoriť diagramy pre všetky
     * kombinácie usporiadania premenných
     *
     * @param paTable funkcia
     * @return vráti zoznam počtu vrcholov.
     */
    public ArrayList<String> exactAlgoritm(MVTable paTable) {
        combinationString.clear();
        int min = 999999;
        int pomNodes = 0;
        String s = "";
        for (int i = 0; i < paTable.getHeadre().length - 1; i++) {
            s += paTable.getHeadre()[i];
        }
        printAllCombString(s);
        ArrayList<String> resultComb = new ArrayList<String>();
        MDDiagram paTree;
        for (int i = 0; i < combinationString.size(); i++) {
            paTree = new MDDiagram(combinationString.get(i), paTable.getFunction());
            paTree.removeSameInnerNodes();
            paTree.removeRedundangEdges();
            pomNodes = paTree.countNodes();
            if (min > pomNodes) {
                resultComb.clear();
                min = pomNodes;
                resultComb.add(combinationString.get(i));
            } else if (min == pomNodes) {
                resultComb.add(combinationString.get(i));
            }
        }
        resultComb.add(0, String.valueOf(min));
        return resultComb;
    }

    /**
     * Vytvorí všetky možné kombinácie pre prvky s rovnakou hodnotou a vytvorí
     * možné usporiadania premenných
     *
     * @param pomMapResult dáta pre metódu
     * @param fromMaximumToMinimum parameter slúžiasci na vyber usporiadania od
     * najmenšieho alebo najväčšieho prvku
     * @return vráti zoznam usporiadaní.
     */
    public ArrayList<String> createCombVariableFromValues(HashMap<String, Double> pomMapResult, boolean fromMaximumToMinimum) {
        double min;
        double max;
        String chooseKeys = "";
        ArrayList<String> finalCombination = new ArrayList<String>();
        boolean cont = true;

        while (cont) {
            min = 99999.9;
            max = -1;
            for (Entry<String, Double> entry2 : pomMapResult.entrySet()) {
                if (fromMaximumToMinimum) {
                    if (entry2.getValue() > max) {
                        max = entry2.getValue();
                        chooseKeys = "";
                        chooseKeys += entry2.getKey();
                    } else if (entry2.getValue() == max) {
                        chooseKeys += entry2.getKey();
                    }
                } else {
                    if (entry2.getValue() < min) {
                        min = entry2.getValue();
                        chooseKeys = "";
                        chooseKeys += entry2.getKey();
                    } else if (entry2.getValue() == min) {
                        chooseKeys += entry2.getKey();
                    }
                }
            }

            for (int i = 0; i < chooseKeys.length(); i++) {
                pomMapResult.remove(String.valueOf(chooseKeys.charAt(i)));
            }
            combinationString.clear();
            printAllCombString(chooseKeys);
            if (finalCombination.size() == 0) {
                for (int f = 0; f < combinationString.size(); f++) {
                    finalCombination.add(combinationString.get(f));
                }
            } else {
                int x = 0;
                String poms = "";
                int sizeBeforeAddResult = finalCombination.size();
                for (int i = 0; i < sizeBeforeAddResult; i++) {
                    poms = finalCombination.get(x);
                    for (int j = 0; j < combinationString.size(); j++) {
                        if (j == 0) {
                            finalCombination.set(x, poms + combinationString.get(j));
                        } else {
                            finalCombination.add(x, poms + combinationString.get(j));
                        }
                        x++;
                    }
                }
            }
            if (pomMapResult.size() == 0) {
                cont = false;
            }
        }
        return finalCombination;
    }

    /**
     * Vráti najmenšie možné hodnoty z dát
     *
     * @param pomMap dáta pre metódu
     * @return vráti najmenšie hodnoty.
     */
    public HashMap<String, Double> chooseMinimalValueFromMap(HashMap<String, Double> pomMap) {
        double min = 999999999;
        for (Entry<String, Double> entry : pomMap.entrySet()) {
            if (min > entry.getValue()) {
                min = entry.getValue();
            }
        }
        Iterator<Map.Entry<String, Double>> iter = pomMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Double> entry = iter.next();
            if (entry.getValue() != min) {
                iter.remove();
            }
        }
        return pomMap;
    }

    /**
     * Vráti index vo funkcii
     *
     * @param s retazec funkcie
     * @param uniqNumber jedinečné hodnoty funkcie
     * @param numberVariable počet parametrov funkcie
     * @return index vo funkcii
     */
    public int getNumberIndexInArrayFromString(String s, int uniqNumber, int numberVariable) {
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            int p = ((int) (s.charAt(i) - '0'));
            int f = (int) Math.pow(uniqNumber, ((numberVariable - 1) - i));
            result += f * p;
        }
        return result;
    }

    /**
     * Vráti výsledné hodnoty pre entropiu na základe poznania všetkých
     * premenných aj poznania len jednej premennej
     *
     * @param paTable funkcia
     * @param cisla počet hodnot funkcie
     * @return index vo funkcii
     */
    @SuppressWarnings("unchecked")
    public String[][] entropy(MVTable paTable, char cisla[]) {
        boolean firstTime = true;
        String[][] finalResults = new String[2][2];
        HashMap<String, Double>[] firstResults = new HashMap[2];
        HashMap<String, Double> mapResult = new HashMap<>();
        HashMap<String, Double> pomMapResult = new HashMap<>();

        for (int i = 0; i < paTable.getHeadre().length - 1; i++) {
            if (firstTime) {
                firstResults = entropyCheck(paTable, cisla, "", true);
                mapResult = firstResults[0];
                firstTime = false;
            } else {
                if (mapResult.size() > 1) {
                    mapResult = chooseMinimalValueFromMap(mapResult);
                }
                for (Entry<String, Double> entry : mapResult.entrySet()) {
                    for (Entry<String, Double> entry2 : entropyCheck(paTable, cisla, entry.getKey(), false)[0].entrySet()) {
                        pomMapResult.put(entry2.getKey(), entry2.getValue());
                    }
                }
                mapResult.clear();
                for (Entry<String, Double> entry2 : pomMapResult.entrySet()) {
                    mapResult.put(entry2.getKey(), entry2.getValue());
                }
                pomMapResult.clear();
            }
        }
        pomMapResult = firstResults[1];
        Set<String> keySet = mapResult.keySet();
        ArrayList<String> listOfKeys = new ArrayList<String>(keySet);

        finalResults[0] = decideBestCombination(listOfKeys, paTable.getFunction()); // po vypocte celej entropie
        finalResults[1] = decideBestCombination(createCombVariableFromValues(pomMapResult, false), paTable.getFunction()); // po vypocte po entropii jednej premennej    
        return finalResults;
    }

    /**
     * Vráti najlepšiu premennu do usporiadania
     *
     * @param paTable funkcia
     * @param cisla počet hodnot funkcie
     * @param vlozene aktuálne vložené hodnoty do usporiadania
     * @param oneVariable pre výpočet entropie za podmienky poznania jednej
     * premennej
     * @return vráti pole máp
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, Double>[] entropyCheck(MVTable paTable, char cisla[], String vlozene, boolean oneVariable) {
        String resultEntrop = "";
        ArrayList<Integer> uzVlozene = new ArrayList<Integer>();
        ArrayList<Integer> zvysne = new ArrayList<Integer>();
        int[][] tab = paTable.getTable();
        HashMap<String, Double>[] results = new HashMap[2];
        HashMap<String, Double> map = new HashMap<>();
        HashMap<String, Double> firstEntrop = new HashMap<>();
        double min;
        double vysledokPremen;
        ArrayList<Integer> vlozeneIndex = convertStringToArrayListInt(vlozene);
        for (int i = 0; i < vlozeneIndex.size(); i++) {
            uzVlozene.add(vlozeneIndex.get(i));
        }
        uzVlozene.add(tab[0].length - 1);
        for (int i = 0; i < tab[0].length; i++) {
            if (!uzVlozene.contains(i)) {
                zvysne.add(i);
            }
        }
        min = 999999;
        Collections.sort(uzVlozene);
        int allZvysne = zvysne.size();
        int uniqNumber = paTable.getFunction().getSizeOfUniqNumberInFunc();

        int[] probabilityArr = new int[(int) Math.pow(uniqNumber, uzVlozene.size() + 1)];
        for (int x = 0; x < allZvysne; x++) {
            vysledokPremen = 0.0;
            uzVlozene.add(zvysne.get(x));
            for (int i = 0; i < probabilityArr.length; i++) {
                probabilityArr[i] = 0;
            }
            for (int j = 0; j < tab.length; j++) {
                String pomS = "";
                for (int l = 0; l < uzVlozene.size(); l++) {
                    pomS += String.valueOf(tab[j][uzVlozene.get(l)]);
                }
                int f = getNumberIndexInArrayFromString(pomS, uniqNumber, uzVlozene.size());
                probabilityArr[f]++;
            }

            for (int i = 0; i < probabilityArr.length; i++) {
                if (probabilityArr[i] > 0) {
                    double skutocPravd = (double) probabilityArr[i] / (double) tab.length;
                    vysledokPremen += (-skutocPravd) * (Math.log(skutocPravd) / Math.log(2));
                }
            }

            if (min > vysledokPremen) {
                min = vysledokPremen;
                map.clear();
                resultEntrop = vlozene + Character.toString((char) (97 + zvysne.get(x)));
                map.put(resultEntrop, min);
            } else if (min == vysledokPremen) {
                resultEntrop = vlozene + Character.toString((char) (97 + zvysne.get(x)));
                map.put(resultEntrop, min);
            }
            if (oneVariable) {
                firstEntrop.put((Character.toString((char) (97 + zvysne.get(x)))), vysledokPremen);
            }
            uzVlozene.remove(zvysne.get(x));
        }
        results[0] = map;
        results[1] = firstEntrop;
        return results;
    }

    public String convertArrayListIntToString(ArrayList<Integer> pom) {
        String result = "";
        for (int p : pom) {
            result += Character.toString((char) (97 + p));
        }
        return result;
    }

    /**
     * Zmení retazec na list čisiel
     *
     * @param s retaze funkcie
     * @return vráti zoznam čisiel
     */
    public ArrayList<Integer> convertStringToArrayListInt(String s) {
        ArrayList<Integer> pom = new ArrayList<Integer>();
        for (int i = 0; i < s.length(); i++) {
            pom.add(s.charAt(i) - 97);
        }
        return pom;
    }

    /**
     * Výpočet heuristiky pre logické derivácie
     *
     * @param paTable reprezentácia funkcie tabuľkou
     * @return vráti výslednú maticu
     */
    public String[][] logicDerivation(MVTable paTable) {
        char[] pomArr = paTable.getFunction().getArrayUniqNumberFunctionChar();
        char[] pomHeader = paTable.getHeadre();
        String[][] resultArray = new String[pomHeader.length - 1][4];
        int resultDirect = 0;
        int resultInverse = 0;
        
       

        for (int j = 0; j < pomHeader.length - 1; j++) {
            resultDirect = 0;
            resultInverse = 0;
            for (int k = 0; k < pomArr.length - 1; k++) {
                for (int l = 1; l < pomArr.length; l++) {
                    paTable.derive(j, Character.getNumericValue(pomArr[k]), Character.getNumericValue(pomArr[l]), Character.getNumericValue(pomArr[k]), Character.getNumericValue(pomArr[l]));
                    int a = paTable.getResultDerivationFromTable();                   
                    
                    resultDirect += paTable.getResultDerivation();

                    paTable.derive(j, Character.getNumericValue(pomArr[k]), Character.getNumericValue(pomArr[l]), Character.getNumericValue(pomArr[l]), Character.getNumericValue(pomArr[k]));
                    resultInverse += paTable.getResultDerivationFromTable();
                }
            }
            resultArray[j][0] = Character.toString(pomHeader[j]);
            resultArray[j][1] = Integer.toString(resultDirect);
            resultArray[j][2] = Integer.toString(resultInverse);
            resultArray[j][3] = Integer.toString(resultInverse + resultDirect);
        }
        return resultArray;
    }

    /**
     * Výpočíta najlepšie usporiadanie
     *
     * @param resultArray výsledná tabuľka po logických deriváciách
     * @param paFunction funkcia
     * @return vráti výslednú maticu
     */
    public String[] getBestPathFromDerivation(String[][] resultArray, MVFunction paFunction) {
        HashMap<String, Double> mapResult = new HashMap<>();;
        int lastIndexTable = resultArray[0].length - 1;
        for (int i = 0; i < resultArray.length; i++) {
            mapResult.put(Character.toString((char) (97 + i)), Double.parseDouble(resultArray[i][lastIndexTable]));
        }
        ArrayList<String> allCombAfterDeriv = createCombVariableFromValues(mapResult, true);
        return decideBestCombination(allCombAfterDeriv, paFunction);
    }

    /**
     * Vyskúša vytvoriť všetky stromy na základe zoznamu usporiadani a vráti
     * najlepšie usporiadanie
     *
     * @param combination zoznam usporiadaní
     * @param paFunction funkcia
     * @return vráti najlepšie usporiadanie
     */
    public String[] decideBestCombination(ArrayList<String> combination, MVFunction paFunction) {
        String minimumPath = "";
        String[] result = new String[2];
        int minCounNodes = 9999999;
        MDDiagram paTree;       
        for (int i = 0; i < combination.size(); i++) {
            paTree = new MDDiagram(combination.get(i), paFunction);
            paTree.removeSameInnerNodes();
            paTree.removeRedundangEdges();
            if (paTree.countNodes() < minCounNodes) {
                minCounNodes = paTree.countNodes();
                minimumPath = paTree.getPath();
            }
        }
        result[0] = String.valueOf(minCounNodes);
        result[1] = minimumPath;
        return result;
    }

    /**
     * Zavolá metódu printAllKLengthCombRec
     *
     * @param set množina znakov pre kombinácie
     * @param k dlžka kombinácie
     * @param paComb zoznam všetkých vytvorených kombinácií
     */
    public void printAllKlenghtComb(char set[], int k, ArrayList<String> paComb) {
        int n = set.length;
        printAllKLengthCombRec(set, "", n, k, paComb);
    }

    /**
     * Rekurzívne vytvára usporiadanie premenných pre všetky kombinácie určtej
     * dĺžky
     *
     * @param set množina znakov pre kombinácie
     * @param prefix vytvorená kombinácia
     * @param n veľkosť množiny
     * @param k dlžka kombinácie
     * @param paComb zoznam všetkých vytvorených kombinácií
     */
    public void printAllKLengthCombRec(char set[], String prefix, int n, int k, ArrayList<String> paComb) {
        if (k == 0) {
            paComb.add(prefix);
            return;
        }
        for (int i = 0; i < n; ++i) {
            String newPrefix = prefix + set[i];
            printAllKLengthCombRec(set, newPrefix, n, k - 1, paComb);
        }
    }

    /**
     * Zavolá metódu printAllCombStringRec
     *
     * @param string retazec premenných
     */
    public void printAllCombString(String string) {
        printAllCombStringRec(string, "");
    }

    /**
     * Rekurzívne vytvára usporiadanie premenných pre všetky kombinácie
     *
     * @param string reťazec premenných
     * @param permutation vysledná hodnota
     */
    public void printAllCombStringRec(String string, String permutation) {
        if (string.length() == 0) {
            this.combinationString.add(permutation);
            return;
        }
        for (int i = 0; i < string.length(); i++) {
            char toAppendToPermutation = string.charAt(i);
            String remaining = string.substring(0, i) + string.substring(i + 1);
            printAllCombStringRec(remaining, permutation + toAppendToPermutation);
        }
    }
}
