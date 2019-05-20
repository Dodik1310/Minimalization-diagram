package Logic;

import java.util.ArrayList;

/**
 *
 * @author Dominik
 */
class Node {

    private char name;
    private String iD;
    private int numberChild;
    private String pathToNode;
    ArrayList<Node> childs;
    ArrayList<Node> parent;

    /**
     * Konštruktor vytvorí nový vrchol s menom a identifikátorom
     *
     * @param name meno vrcholu
     * @param nam identifikátor vrcholu
     * @param numberChild počet potomkov
     */
    Node(char name, String nam, int numberChild) {
        this.name = name;
        this.iD = nam;
        this.numberChild = numberChild;
        parent = new ArrayList<Node>();
        childs = new ArrayList<Node>();
        pathToNode = "";
    }

    /**
     * Getter počet potomkov vrcholu
     *
     * @return vráti počet potomkov
     */
    public int getNumberChild() {
        return numberChild;
    }

    /**
     * Getter pre cestu premennej od koreňa
     *
     * @return vráti cestu
     */
    public String getPathToNode() {
        return pathToNode;
    }

    /**
     * Pridá novu hranu k ceste od koreňa
     *
     * @param branchParent pridávaný reťazec
     */
    public void addToPathToNode(String branchParent) {
        this.pathToNode += branchParent;
    }

    /**
     * Vymaže cestu
     */
    public void clearPathToNode() {
        this.pathToNode = "";
    }

    /**
     * Getter pre zoznam potomkov
     *
     * @return vráti zoznam
     */
    public ArrayList<Node> getChild() {
        return childs;
    }

    /**
     * Prídavanie do zoznamu potomkov
     *
     * @param n pridávaný potomok
     */
    public void addChild(Node n) {
        childs.add(n);
    }

    /**
     * Prídavanie do zoznamu predchodcov
     *
     * @param n pridávaný predchodca
     */
    public void addParent(Node n) {
        parent.add(n);
    }

    /**
     * Prídavanie do zoznamu predchodcov
     *
     * @param n zoznam pridávaný predchodca
     */
    public void addParent(ArrayList<Node> n) {
        for (Node pom : n) {
            parent.add(pom);
        }
    }

    /**
     * Getter pre označenie vrcholu
     *
     * @return vráti označenie
     */
    public char getName() {
        return name;
    }

    /**
     * Getter pre jedinečné označenie vrcholu
     *
     * @return vráti označenie
     */
    public String getId() {
        return iD;
    }

    /**
     * Getter pre zozname predchodcov
     *
     * @return vráti zoznam
     */
    public ArrayList<Node> getParent() {
        return parent;
    }

    /**
     * Odstránenie všetkých prvkov zoznamov predchodcov a potomkov
     */
    public void clearNode() {
        parent.clear();
        childs.clear();
    }

    /**
     * Vypis vrcholu v požadovanom tvare
     *
     * @return vráti reťazec
     */
    public String toString() {
        String s;
        s = "Vrchol: " + iD + " a jeho rodic ";
        if (parent.size() > 0) {
            for (int i = 0; i < parent.size(); i++) {
                s += parent.get(i).getId();
            }
        }
        return s;
    }

    /**
     * Porovnanie rovnosti dvoch vrcholov
     *
     * @return ak sa rovnaju vráti true inak false
     */
    public boolean compareNode(Node n) {
        for (int i = 0; i < childs.size(); i++) {
            if (!(childs.get(i).getId().equals(n.getChild().get(i).getId()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Nájde index vrcholu v zozname potomkov
     *
     * @param n hľadaný vrchol
     * @return ak sa rovnaju vráti true inak false
     */
    public int findIndexChildNode(Node n) {
        return childs.indexOf(n);
    }

    /**
     * Nastáví nový vrchol v zozname na základe indexu a vrcholu
     *
     * @param index index v zozname
     * @param n nový vrchol
     */
    public void setChildOnIndex(int index, Node n) {
        childs.set(index, n);
    }

    /**
     * Zisťuje či sa všetci potomkovia majú rovnakú hodnotu
     *
     * @return ak áno vráti true inak false
     */
    public boolean sameAllChild() {
        if (childs.size() > 0) {
            String s = childs.get(0).getId();
            for (int i = 1; i < childs.size(); i++) {
                if (!(childs.get(i).getId().equals(s))) {
                    return false;
                }
            }
        } else {
            System.out.println("Niesu ziadny potomkovia vrcholu");
            return false;
        }
        return true;
    }

    /**
     * Odstráni vrchol zo zoznamu predchodcov
     *
     * @param n odstraňovaný vrchol
     */
    public void removeParent(Node n) {
        parent.remove(n);
    }
}
