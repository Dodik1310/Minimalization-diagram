package Graphic;

import Logic.Algorithms;
import Logic.MDDiagram;
import Logic.MVFunction;
import Logic.MVTable;
import Logic.Tests;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 *
 * @author Dominik
 */
public class ProgramUI extends javax.swing.JFrame {

    MVFunction function;
    MVTable table;
    MDDiagram tree;

    Algorithms algor;
    BufferedImage bufImg;
    Tests test;

    boolean pomVariableCompo = false;
    boolean firstTimeVar;
    boolean firstTimeFunct;

    /**
     * Vytvorí grafické komponenty testy a algoritmy
     */
    public ProgramUI() throws IOException {
        initComponents();
        algor = new Algorithms();
        test = new Tests();
        loadFunctionFromFile();
        setEnableButtCreateFunct(false);
        hideTexts();

    }

    /**
     * Načíta všetky predpripravené funkcie zo suboru
     *
     * @throws IOException pripad, ak sa nepodarí načitať
     */
    public void loadFunctionFromFile() throws FileNotFoundException, IOException {
        int itemCount = zoznamFunk.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            zoznamFunk.removeItemAt(0);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("funkcie/funkcieNacit.txt")));
        try {
            String s;
            String cis = "";
            while ((s = br.readLine()) != null) {
                this.zoznamFunk.addItem(s);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "Graf sa nepodarilo nacitat");
        } finally {
            br.close();

        }
    }

    /**
     * Načíta funkciu zo suboru
     *
     * @param subor súbor, z ktorého sa načítava
     * @throws IOException pripad, ak sa nepodarí načitať
     */
    public void readFunction(String subor) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("funkcie/funkcieNacit.txt")));
        try {
            String s;
            String cis = "";
            while ((s = br.readLine()) != null) {
                if (subor.equals(s)) {
                    cis = s;
                    this.funcString.setText(s);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "Graf sa nepodarilo nacitat");
        } finally {
            br.close();
        }
    }

    /**
     * Skontroluje vyplnenie hodnôt pre funkcie
     *
     * @return true ak je spravne inak false
     */
    public boolean checkTextForCreateFunction() {
        if (funcString.getText().length() == 0 || fieldNumberUniqValue.getText().length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Metóda pred vytvorením funkcie
     */
    public void beforeCreateFunction() {
        setEnableButtCreateFunct(false);
        pomVariableCompo = false;
        firstTimeVar = true;
        firstTimeFunct = true;
        changeFuncTo.setEnabled(false);
        changeVarTo.setEnabled(false);
        changeFuncTo.removeAllItems();
        changeVarTo.removeAllItems();
        variableCombBox.removeAllItems();
        this.textTab.setVisible(true);
        clearApplicationBeforeNewFunction();
    }

    /**
     * Vytvorí tabuľku funkcie do grafického prostredia
     */
    public void createTableDisp() {
        beforeCreateFunction();
        if (checkTextForCreateFunction() && checkIfOnlyNumberInText(fieldNumberUniqValue.getText()) && checkIfOnlyNumberInText(funcString.getText())) {
            String s = this.funcString.getText();
            int uniqNumb = Integer.parseInt(fieldNumberUniqValue.getText());
            function = new MVFunction(s, uniqNumb);
            if (function.checkFunction()) {
                table = new MVTable(function);
                String[] riadok = new String[function.getNumberOfVariable() + 1];
                for (int i = 0; i < riadok.length; i++) {
                    riadok[i] = String.valueOf(table.getHeadre()[i]);

                }
                Object[][] data = new Object[table.getTable().length][table.getTable()[0].length];
                for (int i = 0; i < table.getTable().length; i++) {
                    for (int j = 0; j < table.getTable()[0].length; j++) {
                        data[i][j] = table.getTable()[i][j];
                    }
                }
                tableFunction.setModel(new javax.swing.table.DefaultTableModel(data, riadok));
                for (int i = 0; i < table.getTable()[0].length - 1; i++) {
                    this.variableCombBox.addItem(String.valueOf(table.getHeadre()[i]));
                }
                changeVarFrom.removeAllItems();
                changeVarFrom.addItem("-");
                for (int i = 0; i < function.getArrayUniqNumberFunctionChar().length; i++) {
                    changeVarFrom.addItem(String.valueOf(function.getArrayUniqNumberFunctionChar()[i]));
                }

                changeFuncFrom.removeAllItems();
                changeFuncFrom.addItem("-");
                for (int i = 0; i < function.getArrayUniqNumberFunctionChar().length; i++) {
                    changeFuncFrom.addItem(String.valueOf(function.getArrayUniqNumberFunctionChar()[i]));
                }
                pomVariableCompo = true;
                this.textTab.setVisible(true);
                setEnableButtCreateFunct(true);
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Nesprávne zadaná funkcia alebo počet hodnôt funkcie.");
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Nezadané hodnoty pre funkciu vytvorenie funkcie.", "Upozornenie", 2);
        }
    }

    /**
     * Zderivuje funkciu podľa zadaných parametrov a vytvorí tabuľku funkcie po
     * derivácii
     */
    public void deriveDisp() {
        String stlpec = this.variableCombBox.getSelectedItem().toString();
        char a = stlpec.charAt(0);
        int stlp = a - 'a';
        table.derive(stlp, Integer.parseInt(changeVarFrom.getSelectedItem().toString()), Integer.parseInt(changeVarTo.getSelectedItem().toString()),
                Integer.parseInt(changeFuncFrom.getSelectedItem().toString()), Integer.parseInt(changeFuncTo.getSelectedItem().toString()));

        String[] riadok = new String[table.getTableDerivation()[0].length];
        for (int i = 0; i < table.getHeadreDerive().length; i++) {
            riadok[i] = String.valueOf(table.getHeadreDerive()[i]);
        }
        Object[][] data = new Object[table.getTableDerivation().length][table.getTableDerivation()[0].length];
        for (int i = 0; i < table.getTableDerivation().length; i++) {
            for (int j = 0; j < table.getTableDerivation()[0].length; j++) {
                data[i][j] = table.getTableDerivation()[i][j];
            }
        }
        this.derTabulka.setModel(new javax.swing.table.DefaultTableModel(data, riadok));
        this.textTabDeriv.setVisible(true);

    }

    /**
     * Vytvorí graf podľa zadaného usporiadania premenných
     *
     * @throws IOException ak sa nepodarí zapísať do súboru
     */
    public void createGraphDis() throws IOException, InterruptedException {
        String s = orderGraph.getText();
        tree = new MDDiagram(s, function);
        tree.removeSameInnerNodes();
        tree.removeRedundangEdges();
        numberCreateTree.setText("Vytvorený graf s počtom vnútorných vrcholov: " + tree.countNodes());
        numberCreateTree.setVisible(true);
        try {
            tree.saveToFile("diagram/graf");
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("dot -Tjpg diagram/graf.dot -o diagram/graf.jpg");
            this.vykresli.setEnabled(true);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(new JFrame(), "K vykresleniu diagramu je potrebná aplikácia Graphviz.");
        }
    }

    /**
     * Skontroluje správnosť zapísaných premenných diagramu
     *
     * @throws IOException ak sa nepodarí zapísať do súboru
     */
    public void checkVariableTreeAndCreate() throws IOException {
        String varOrder = orderGraph.getText();
        ArrayList<Character> varTree = new ArrayList<Character>();

        for (int i = 0; i < varOrder.length(); i++) {
            varTree.add(varOrder.charAt(i));
        }

        for (int i = 0; i < table.getHeadre().length - 1; i++) {
            for (int j = 0; j < varTree.size(); j++) {
                if (table.getHeadre()[i] == varTree.get(j)) {
                    varTree.remove(j);
                    break;
                }
            }
        }
        if (varOrder.length() == table.getHeadre().length - 1) {
            if (varTree.size() == 0) {
                try {
                    this.createGraphDis();
                } catch (IOException ex) {
                    Logger.getLogger(ProgramUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProgramUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                numberCreateTree.setText("");
                this.jLabel8.setIcon(null);
                this.jLabel8.revalidate();
                JOptionPane.showMessageDialog(new JFrame(), "Nesprávne zadané premenné diagramu.");
            }
        } else {
            numberCreateTree.setText("");            
            this.jLabel8.setIcon(null);
            this.jLabel8.revalidate();
            JOptionPane.showMessageDialog(new JFrame(), "Nie su zadané premmenné diagramu alebo počet premenných diagramu je nesprávny.");
        }
    }

    /**
     * Urobí heuristiku založenú na entropii a vypíše hu do grafického
     * prostredia
     */
    public void calculateEntropy() {
        String[][] res = algor.entropy(table, function.getArrayUniqNumberFunctionChar());
        resultEnt.setText(res[0][1]);
        resultFirstEntropy.setText(res[1][1]);
        this.textEntropyFirst.setVisible(true);
        this.textEntrop.setVisible(true);
        this.resultEnt.setVisible(true);
        this.resultFirstEntropy.setVisible(true);
    }

    /**
     * Urobí heuristiku logických derivácií a vypíše do grafického prostredia
     */
    public void calculateLogicDerivation() {
        String[][] array = algor.logicDerivation(table);
        String[] res = algor.getBestPathFromDerivation(array, function);
        resultDer.setText(res[1]);
        String[] riadok = new String[4];
        riadok[0] = "";
        riadok[1] = "Direct";
        riadok[2] = "Inverse";
        riadok[3] = "SUM";
        Object[][] data = new Object[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                data[i][j] = array[i][j];
            }
        }
        this.tabPocetPremen.setModel(new javax.swing.table.DefaultTableModel(data, riadok));
        this.textDeriv.setVisible(true);
        this.resultDer.setVisible(true);
    }

    /**
     * Urobí exaktný algoritmus a vypíše do grafického prostredia
     */
    public void calculateExactAlgor() {
        ArrayList<String> pocVrchol = algor.exactAlgoritm(table);
        this.textExactAlgor.setText("Kombinácie s minimálnym počtom vrcholov: " + pocVrchol.get(0));
        pocVrchol.remove(0);
        DefaultTableModel tabkom;
        tabkom = new DefaultTableModel();;
        this.tabulkaKomb.setModel(tabkom);
        tabkom.addColumn("Poradie premenných");
        for (int j = 0; j < pocVrchol.size(); j++) {
            tabkom.addRow(new Object[]{pocVrchol.get(j)});
        }
        this.textExactAlgor.setVisible(true);
    }

    /**
     * Zablokuje určité tlačidlá
     */
    public void setEnableButtCreateFunct(boolean enable) {
        this.exactBut.setEnabled(enable);
        this.deriveButtHeuristic.setEnabled(enable);
        this.entropButt.setEnabled(enable);
        this.vykresli.setEnabled(enable);
        this.vgraf.setEnabled(enable);
    }

    /**
     * Vymaže určité grafické elementy
     */
    public void clearApplicationBeforeNewFunction() {
        this.tableFunction.setModel(new DefaultTableModel());
        this.derTabulka.setModel(new DefaultTableModel());
        this.tabPocetPremen.setModel(new DefaultTableModel());
        this.tabulkaKomb.setModel(new DefaultTableModel());
        this.jLabel8.setIcon(null);
        this.jLabel8.revalidate();
        this.textExactAlgor.setText("");
        this.orderGraph.setText("");
        this.hideTexts();
    }

    /**
     * Skryje určité grafické elementy
     */
    public void hideTexts() {
        this.textDeriv.setVisible(false);
        this.resultDer.setVisible(false);
        this.textEntropyFirst.setVisible(false);
        this.textEntrop.setVisible(false);
        this.resultEnt.setVisible(false);
        this.resultFirstEntropy.setVisible(false);
        this.textTab.setVisible(false);
        this.textTabDeriv.setVisible(false);
        this.textExactAlgor.setVisible(false);
        deriveButt.setEnabled(false);
        numberCreateTree.setVisible(false);
    }

    /**
     * Skontroluje či sa v zadanom režazci nachádzaju len čisla
     *
     * @param subor reťazec
     */
    public boolean checkIfOnlyNumberInText(String pomText) {
        for (char pom : pomText.toCharArray()) {
            if (pom < 48 || pom > 57) {
                return false;
            }
        }
        return true;
    }

    /**
     * Skontroluje zadané reťazce pred testovaním
     */
    public boolean checkDataBeforeTest() {
        if (numberTestsFunction.getText().length() != 0 && numberVariableTest.getText().length() != 0 && numberUniqValueTest.getText().length() != 0) {
            if (!checkIfOnlyNumberInText(numberTestsFunction.getText())
                    || !checkIfOnlyNumberInText(numberVariableTest.getText())
                    || !checkIfOnlyNumberInText(numberUniqValueTest.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Zadane hodnoty pre test nie su cisla.");
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Nie su zadané vsetky hodnoty pre test.");
            return false;
        }
        return true;
    }

    /**
     * Vytvorí test
     */
    public void createTest() throws IOException {
        if (checkDataBeforeTest()) {
            if(test.testFunction(Integer.parseInt(numberUniqValueTest.getText()), Integer.parseInt(numberVariableTest.getText()), Integer.parseInt(numberTestsFunction.getText()))){
                 JOptionPane.showMessageDialog(new JFrame(), "Test prebehol úspešne");
                
            }
        }                
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        deriveButt = new javax.swing.JButton();
        variableCombBox = new javax.swing.JComboBox<>();
        changeVarFrom = new javax.swing.JComboBox<>();
        changeFuncFrom = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        derTabulka = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        }
        ;
        textTabDeriv = new javax.swing.JLabel();
        changeVarTo = new javax.swing.JComboBox<>();
        changeFuncTo = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        textExactAlgor = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tabulkaKomb = new javax.swing.JTable();
        exactBut = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        entropButt = new javax.swing.JButton();
        resultEnt = new javax.swing.JTextField();
        textEntrop = new javax.swing.JLabel();
        textEntropyFirst = new javax.swing.JLabel();
        resultFirstEntropy = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        numberVariableTest = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        numberUniqValueTest = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        numberTestsFunction = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        deriveButtHeuristic = new javax.swing.JButton();
        resultDer = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabPocetPremen = new javax.swing.JTable();
        textDeriv = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        vgraf = new javax.swing.JButton();
        orderGraph = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jLabel8 = new javax.swing.JLabel();
        vykresli = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        numberCreateTree = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        nacitajFunciu = new javax.swing.JButton();
        zoznamFunk = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableFunction = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        vytvorTabulku = new javax.swing.JButton();
        textMonoton = new javax.swing.JLabel();
        fieldNumberUniqValue = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        textTab = new javax.swing.JLabel();
        funcString = new javax.swing.JTextField();

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Minimalizácia viachodnotových rozhodovacích diagramov");

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Vyber premennú: ");

        jLabel3.setText("Vyber zmenu premennej:");

        jLabel4.setText("Vyber zmenu funkcie:");

        deriveButt.setText("Derivuj");
        deriveButt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deriveButtActionPerformed(evt);
            }
        });

        changeVarFrom.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                changeVarFromItemStateChanged(evt);
            }
        });

        changeFuncFrom.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                changeFuncFromItemStateChanged(evt);
            }
        });

        jScrollPane3.setBorder(null);

        derTabulka.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane3.setViewportView(derTabulka);

        textTabDeriv.setText("Výsledná tabuľka derivácie:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(changeFuncFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(deriveButt)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(changeVarFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(35, 35, 35)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(variableCombBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(changeVarTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(changeFuncTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addComponent(textTabDeriv)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(variableCombBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(changeVarFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changeVarTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeFuncFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(changeFuncTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(deriveButt)
                .addGap(14, 14, 14)
                .addComponent(textTabDeriv)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(154, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Smerové parciálne derivácie", jPanel1);

        textExactAlgor.setText("Kombinácie s minimálnym počtom vrcholov: ");

        jScrollPane6.setBorder(null);

        tabulkaKomb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tabulkaKomb.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(tabulkaKomb);

        exactBut.setText("Vypočítaj všetky kombinácie");
        exactBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exactButActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textExactAlgor)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exactBut))
                .addContainerGap(111, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(exactBut)
                .addGap(31, 31, 31)
                .addComponent(textExactAlgor, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(369, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Exaktný algoritmus", jPanel2);

        entropButt.setText("Vypočítaj poradie premenných ");
        entropButt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entropButtActionPerformed(evt);
            }
        });

        resultEnt.setEditable(false);

        textEntrop.setText("Poradie premenných na základe všetkých premenných: ");

        textEntropyFirst.setText("Poradie premenných na základe jednej premenneji:");

        resultFirstEntropy.setEditable(false);
        resultFirstEntropy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resultFirstEntropyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(textEntropyFirst)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(entropButt)
                            .addComponent(textEntrop))
                        .addGap(38, 51, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(resultFirstEntropy, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(resultEnt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(entropButt)
                .addGap(18, 18, 18)
                .addComponent(textEntrop, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultEnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textEntropyFirst)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(resultFirstEntropy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(431, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Entropia heuristika", jPanel4);

        jLabel9.setText("Zadaj počet premenných:");

        jLabel10.setText("Zadaj počet hodnôt funkcie:");

        jButton1.setText("Otestuj");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel11.setText("Počet testovaných funkcii:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(numberVariableTest, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(numberTestsFunction, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(numberUniqValueTest, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(numberUniqValueTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numberVariableTest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numberTestsFunction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(442, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Test", jPanel5);

        deriveButtHeuristic.setText("Vypočítaj poradie premenných ");
        deriveButtHeuristic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deriveButtHeuristicActionPerformed(evt);
            }
        });

        resultDer.setEditable(false);

        jScrollPane5.setBorder(null);

        tabPocetPremen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tabPocetPremen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(tabPocetPremen);

        textDeriv.setText("Vysledné poradie premenných:  ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(deriveButtHeuristic)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(textDeriv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addComponent(resultDer, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(deriveButtHeuristic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resultDer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textDeriv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(348, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Derivácie heuristika", jPanel3);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        vgraf.setText("Vytvor graf");
        vgraf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vgrafActionPerformed(evt);
            }
        });

        jLabel5.setText("Zadaj poradie premenných grafu:");

        jScrollPane4.setBorder(null);
        jScrollPane4.setViewportView(jLabel8);

        vykresli.setText("Vykresli graf");
        vykresli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vykresliActionPerformed(evt);
            }
        });

        numberCreateTree.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        numberCreateTree.setText("Pocet vrcholov");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(numberCreateTree)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(242, 242, 242)
                                .addComponent(jLabel6)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(orderGraph, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(vgraf)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(vykresli)
                                .addContainerGap(163, Short.MAX_VALUE))))))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(orderGraph, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vgraf)
                    .addComponent(vykresli))
                .addGap(9, 9, 9)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numberCreateTree)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4)
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        nacitajFunciu.setText("Načítaj funkciu");
        nacitajFunciu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nacitajFunciuActionPerformed(evt);
            }
        });

        zoznamFunk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0101", "00000111", "01010100", "1011001111011100", "0100111110110010" }));
        zoznamFunk.setMaximumSize(new java.awt.Dimension(121, 20));
        zoznamFunk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoznamFunkActionPerformed(evt);
            }
        });

        jLabel1.setText("Zadaj funkciu:");

        jScrollPane2.setBorder(null);

        tableFunction.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane2.setViewportView(tableFunction);

        vytvorTabulku.setText("Vytvor funkciu");
        vytvorTabulku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vytvorTabulkuActionPerformed(evt);
            }
        });

        fieldNumberUniqValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldNumberUniqValueActionPerformed(evt);
            }
        });

        jLabel7.setText("Počet hodnôt funkcie:");

        textTab.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        textTab.setText("Vytvorená tabuľka:");

        funcString.setMaximumSize(new java.awt.Dimension(6, 20));
        funcString.setPreferredSize(new java.awt.Dimension(210, 25));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textTab)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nacitajFunciu)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textMonoton)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(vytvorTabulku)
                                    .addComponent(zoznamFunk, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldNumberUniqValue, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(funcString, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(funcString, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(textMonoton, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(74, 74, 74))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fieldNumberUniqValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(9, 9, 9)
                        .addComponent(vytvorTabulku)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(zoznamFunk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nacitajFunciu))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textTab)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void vytvorTabulkuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vytvorTabulkuActionPerformed
        createTableDisp();
    }//GEN-LAST:event_vytvorTabulkuActionPerformed

    private void deriveButtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deriveButtActionPerformed
        this.textTabDeriv.setVisible(true);
        this.deriveDisp();
    }//GEN-LAST:event_deriveButtActionPerformed

    private void vgrafActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vgrafActionPerformed
        try {
            checkVariableTreeAndCreate();
        } catch (IOException ex) {
            Logger.getLogger(ProgramUI.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_vgrafActionPerformed

    private void nacitajFunciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nacitajFunciuActionPerformed
        try {
            this.readFunction(this.zoznamFunk.getSelectedItem().toString());
        } catch (IOException ex) {
            Logger.getLogger(ProgramUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_nacitajFunciuActionPerformed

    private void zoznamFunkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoznamFunkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_zoznamFunkActionPerformed

    private void deriveButtHeuristicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deriveButtHeuristicActionPerformed
        calculateLogicDerivation();
    }//GEN-LAST:event_deriveButtHeuristicActionPerformed

    private void exactButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exactButActionPerformed
        calculateExactAlgor();
    }//GEN-LAST:event_exactButActionPerformed

    private void vykresliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vykresliActionPerformed
        try {
            bufImg = ImageIO.read(new File("diagram/graf.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(ProgramUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        jLabel8.setIcon(new ImageIcon(bufImg));

    }//GEN-LAST:event_vykresliActionPerformed

    private void entropButtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entropButtActionPerformed
        this.resultEnt.setVisible(true);
        this.textEntrop.setVisible(true);
        calculateEntropy();

    }//GEN-LAST:event_entropButtActionPerformed

    private void changeVarFromItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_changeVarFromItemStateChanged
        if (pomVariableCompo) {
            changeVarTo.setEnabled(true);
            if (firstTimeVar) {
                changeVarFrom.removeItem("-");
                firstTimeVar = false;
            }
            changeVarTo.removeAllItems();
            if (changeVarFrom.getItemCount() > 0) {
                for (int i = 0; i < function.getArrayUniqNumberFunctionChar().length; i++) {
                    if (!String.valueOf(function.getArrayUniqNumberFunctionChar()[i]).equals(changeVarFrom.getSelectedItem().toString())) {
                        changeVarTo.addItem(String.valueOf(function.getArrayUniqNumberFunctionChar()[i]));
                    }
                }
            }
        }

        if (changeVarTo.isEnabled() && changeFuncTo.isEnabled()) {
            deriveButt.setEnabled(true);
        }
    }//GEN-LAST:event_changeVarFromItemStateChanged

    private void changeFuncFromItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_changeFuncFromItemStateChanged
        if (pomVariableCompo) {
            changeFuncTo.setEnabled(true);
            if (firstTimeFunct) {
                changeFuncFrom.removeItem("-");
                firstTimeFunct = false;
            }
            changeFuncTo.removeAllItems();
            if (changeFuncFrom.getItemCount() > 0) {
                for (int i = 0; i < function.getArrayUniqNumberFunctionChar().length; i++) {
                    if (!String.valueOf(function.getArrayUniqNumberFunctionChar()[i]).equals(changeFuncFrom.getSelectedItem().toString())) {
                        changeFuncTo.addItem(String.valueOf(function.getArrayUniqNumberFunctionChar()[i]));
                    }
                }
            }
        }
        if (changeVarTo.isEnabled() && changeFuncTo.isEnabled()) {
            deriveButt.setEnabled(true);
        }
    }//GEN-LAST:event_changeFuncFromItemStateChanged

    private void resultFirstEntropyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resultFirstEntropyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resultFirstEntropyActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            createTest();
        } catch (IOException ex) {
            Logger.getLogger(ProgramUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void fieldNumberUniqValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldNumberUniqValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldNumberUniqValueActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProgramUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProgramUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProgramUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProgramUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ProgramUI().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(ProgramUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> changeFuncFrom;
    private javax.swing.JComboBox<String> changeFuncTo;
    private javax.swing.JComboBox<String> changeVarFrom;
    private javax.swing.JComboBox<String> changeVarTo;
    private javax.swing.JTable derTabulka;
    private javax.swing.JButton deriveButt;
    private javax.swing.JButton deriveButtHeuristic;
    private javax.swing.JButton entropButt;
    private javax.swing.JButton exactBut;
    private javax.swing.JTextField fieldNumberUniqValue;
    private javax.swing.JTextField funcString;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton nacitajFunciu;
    private javax.swing.JLabel numberCreateTree;
    private javax.swing.JTextField numberTestsFunction;
    private javax.swing.JTextField numberUniqValueTest;
    private javax.swing.JTextField numberVariableTest;
    private javax.swing.JTextField orderGraph;
    private javax.swing.JTextField resultDer;
    private javax.swing.JTextField resultEnt;
    private javax.swing.JTextField resultFirstEntropy;
    private javax.swing.JTable tabPocetPremen;
    private javax.swing.JTable tableFunction;
    private javax.swing.JTable tabulkaKomb;
    private javax.swing.JLabel textDeriv;
    private javax.swing.JLabel textEntrop;
    private javax.swing.JLabel textEntropyFirst;
    private javax.swing.JLabel textExactAlgor;
    private javax.swing.JLabel textMonoton;
    private javax.swing.JLabel textTab;
    private javax.swing.JLabel textTabDeriv;
    private javax.swing.JComboBox<String> variableCombBox;
    private javax.swing.JButton vgraf;
    private javax.swing.JButton vykresli;
    private javax.swing.JButton vytvorTabulku;
    private javax.swing.JComboBox<String> zoznamFunk;
    // End of variables declaration//GEN-END:variables
}
