package generated;
import parser.*;
public class Parser {
    private final ParsedTree tree = new ParsedTree();

    public Parser() {
        Gencompany company1 = new Gencompany();
        company1.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        company1.setAttribute("xsi:noNamespaceSchemaLocation", "testXSD.xsd");
        tree.addNode(new Node().setSubElement(tree.getNode(1))
                    .setID(0)
                    .setContent(company1));
        Genperson person1 = new Genperson();
        person1.setAttribute("ID", "1");
        person1.setAttribute("name", "Adam");
        tree.addNode(new Node().setSubElement(tree.getNode(2)).setSubElement(tree.getNode(3))
                    .setID(1)
                    .setContent(person1));
        Genperson person2 = new Genperson();
        person2.setAttribute("ID", "2");
        person2.setAttribute("name", "Peter");
        tree.addNode(new Node().setSubElement(tree.getNode(2)).setSubElement(tree.getNode(3))
                    .setID(1)
                    .setContent(person2));
        Genposition position1 = new Genposition();
        position1.setContent("Programmer ");
        tree.addNode(new Node().setID(2)
                    .setContent(position1));
        Genposition position2 = new Genposition();
        position2.setContent("Cleaner ");
        tree.addNode(new Node().setID(2)
                    .setContent(position2));
        Gensalary salary1 = new Gensalary();
        salary1.setContent(10000);
        tree.addNode(new Node().setID(3)
                    .setContent(salary1));
        Gensalary salary2 = new Gensalary();
        salary2.setContent(20000);
        tree.addNode(new Node().setID(3)
                    .setContent(salary2));
    }
}