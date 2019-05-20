package Logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Dominik
 */
public class MDDiagram {

    private Node root;
    private String orderingVar;
    private int idNode;
    private Queue<Node> queueNodes;
    private int[] arrOfPower;
    private ArrayList<Node> nodes;
    private MVFunction function;

    /**
     * Konštruktor, ktorý vytvorí strom na zíklade poradia premenných na zadanej
     * funkcie
     *
     * @param size počet vrcholov
     * @param path poradie premenných
     */
    public MDDiagram(String path, MVFunction pomFunction) {
        root = null;
        function = pomFunction;
        queueNodes = new LinkedList<Node>();
        nodes = new ArrayList<Node>();
        this.orderingVar = path;
        arrOfPower = new int[path.length()];
        fillArrOfPower();
        createTreeInsideNodes();
        addLists();
    }

    /**
     * Vytvorí vnútorné vrcholy diagramu
     */
    public void createTreeInsideNodes() {
        Node n = null;
        int j = 0;
        int m = 0;
        idNode = 0;
        int numberChildrenNode = function.getSizeOfUniqNumberInFunc();
        int size = (function.sizeFunction() - 1) / (function.sizeOfUniqNumberInFunc - 1);
        int pom = 0;
        int pom2 = 0;
        for (int i = 1; i <= size; i++) {
            n = new Node(orderingVar.charAt(j), "" + orderingVar.charAt(j) + idNode, numberChildrenNode);
            idNode++;
            addNode(n);
            pom = (int) Math.pow(numberChildrenNode, m);
            if (i == pom + pom2) {
                pom2 += pom;
                j++;
                m++;
            }
        }
    }

    /**
     * Getter pre koreň diagramu
     *
     * @return vráti koreň
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Getter pre usporiadanie diagramu
     *
     * @return vráti usporiadania
     */
    public String getPath() {
        return orderingVar;
    }

    /**
     * Pridá vrchol na správne miesto
     *
     * @param node vrchol, ktorý chceme pridať
     */
    public void addNode(Node node) {
        if (queueNodes.isEmpty()) {
            root = node;
            queueNodes.add(node);
            nodes.add(node);
        } else {
            Node parent = queueNodes.peek();
            node.addToPathToNode(parent.getPathToNode() + Integer.toString(parent.getChild().size()));
            parent.addChild(node);
            node.addParent(parent);
            queueNodes.add(node);
            nodes.add(node);
            if (parent.getChild().size() == parent.getNumberChild()) {
                queueNodes.poll();
            }
        }
    }

    /**
     * Vráti index vo funkcii
     *
     * @param s cesta od korena k listu.
     */
    public int getNumberForListFromPath(String s) {
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            int p = ((int) (s.charAt(i) - '0'));
            int f = (int) Math.pow(function.getSizeOfUniqNumberInFunc(), arrOfPower[i]);
            result += f * p;
        }
        return result;
    }

    /**
     * Naplní pole hodnot
     */
    public void fillArrOfPower() {
        for (int i = 0; i < arrOfPower.length; i++) {
            arrOfPower[i] = (int) ((orderingVar.length() - 1) - ((int) orderingVar.charAt(i) - 'a'));
        }
    }

    /**
     * Pridá všetky listy stromu
     */
    public void addLists() {
        Node[] arrListNode = new Node[function.getSizeOfUniqNumberInFunc()];
        for (char i : function.getArrayUniqNumberFunctionChar()) {
            arrListNode[Character.getNumericValue(i)] = new Node(i, i + "" + idNode, 0);
            idNode++;
        }

        int s = queueNodes.size();
        Node n = queueNodes.poll();
        Node pomNode = new Node('x', "xx", 0);
        int x = 0;
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < n.getNumberChild(); j++) {
                pomNode.addToPathToNode(n.getPathToNode() + Integer.toString(n.getChild().size()));
                x = function.getFunctionString().charAt(getNumberForListFromPath(pomNode.getPathToNode())) - '0';
                n.addChild(arrListNode[x]);
                pomNode.clearPathToNode();
            }
            n = queueNodes.poll();
        }
    }

    /**
     * Odstráni všetky duplicitné vnútorné vrcholy
     */
    public void removeSameInnerNodes() {
        int numberOfPower = function.getNumberOfVariable() - 1;
        int numberCheckNode = (int) Math.pow(function.getSizeOfUniqNumberInFunc(), numberOfPower);
        int numberStartIndex = nodes.size() - 1;
        int size = numberOfPower + 1;

        for (int i = 0; i < size; i++) {
            for (int j = numberStartIndex; j > numberStartIndex - numberCheckNode + 1; j--) {
                if (nodes.get(j).getParent().size() != 0) {
                    for (int k = j - 1; k > numberStartIndex - numberCheckNode; k--) {
                        if (nodes.get(k).getParent().size() != 0) {
                            if (nodes.get(k).compareNode(nodes.get(j))) {
                                margeNodes(nodes.get(k), nodes.get(j));
                                break;
                            }
                        }
                    }
                }
            }
            numberOfPower--;
            numberStartIndex = (numberStartIndex - numberCheckNode);
            numberCheckNode = (int) Math.pow(function.getSizeOfUniqNumberInFunc(), numberOfPower);
        }
    }

    /**
     * Jeden z dvoch vrcholov odstráni z diagramu
     *
     * @param a duplicitný vrchol.
     * @param b duplicitný vrchol.
     */
    public void margeNodes(Node a, Node b) {
        for (Node pom : b.getParent()) {
            pom.setChildOnIndex(pom.findIndexChildNode(b), a);
        }
        for (Node pom : b.getChild()) {
            pom.removeParent(b);
        }
        a.addParent(b.getParent());
        nodes.remove(b);
        b.clearNode();
    }

    /**
     * Odstránenie prebytočných hrán z diagramu
     */
    public void removeRedundangEdges() {
        int startIndex = nodes.size() - 1;
        for (int i = startIndex; i >= 0; i--) {
            if (nodes.get(i).getParent().size() > 0) {
                if (nodes.get(i).sameAllChild()) {
                    removeRedundantEdgesNode(nodes.get(i));
                }
            }
        }
    }

    /**
     * Odstránenie vrcholu z diagramu
     *
     * @param n odstraňovaný vrchol
     */
    public void removeRedundantEdgesNode(Node n) {
        if (n.getParent().size() > 0) {
            for (Node pom : n.getParent()) {
                pom.setChildOnIndex(pom.findIndexChildNode(n), n.getChild().get(0));
            }
        }
        nodes.remove(n);
        n.clearNode();
    }

    /**
     * Uloží diagram do suboru v požadovanom tvareu
     *
     * @param subor názov súboru
     */
    public void saveToFile(String subor) throws IOException {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(subor + ".dot"));
            bw.write("digraph G{");
            bw.write("");
            Node node;
            for (int i = 0; i < nodes.size(); i++) {
                node = nodes.get(i);
                if (node.getChild().size() > 0) {
                    for (int j = 0; j < node.getChild().size(); j++) {
                        if (node.getChild().get(j).getChild().size() > 0) {
                            bw.write("{" + node.getId() + "[label=" + node.getName() + "]}->"
                                    + "{" + node.getChild().get(j).getId()
                                    + "[label=" + node.getChild().get(j).getName() + "]}[label = " + j + "]");
                        } else {
                            bw.write("{" + node.getId() + "[label=" + node.getName() + "]}->"
                                    + "{" + node.getChild().get(j).getId()
                                    + "[shape=box label=" + node.getChild().get(j).getName() + "]}[label = " + j + "]");
                        }
                        bw.newLine();
                    }
                }
            }
            bw.write("}");
        } catch (Exception e) {
            System.out.println("Do souboru se nepodarilo zapisat.");
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * Vráti počet vrcholov diagramu
     */
    public int countNodes() {
        return nodes.size();
    }

}
