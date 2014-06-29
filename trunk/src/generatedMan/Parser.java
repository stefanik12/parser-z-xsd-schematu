/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generatedMan;

import generated.Gencompany;
import generated.Gensalary;
import generatedMan.*;
import parser.*;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class Parser {
    private final ParsedTree tree = new ParsedTree();

    public Parser() {
        //initialize simpleTypes first
        PositionGen position1 = new PositionGen();
        position1.setContent("Programmer");
        tree.addNode(new Node().setID(1)
                    .setContent(position1));
        //ID is to be set according to XSD Collection

        Gensalary salary1 = new Gensalary();
        salary1.setContent(10000);
        tree.addNode(new Node().setID(2)
                    .setContent(salary1));
        //ID is to be set according to XSD Collection

        PositionGen position2 = new PositionGen();
        position1.setContent("Cleaner");
        tree.addNode(new Node().setID(3)
                    .setContent(position2));
        //ID is to be set according to XSD Collection

        Gensalary salary2 = new Gensalary();
        salary2.setContent(20000);
        tree.addNode(new Node().setID(4)
                    .setContent(salary2));
        //ID is to be set according to XSD Collection

        //afterwards complexTypes and use simpleTypes there
        PersonGen person1 = new PersonGen();
        person1.setAttribute("name", "Adam");
        person1.setAttribute("ID", "1");
        tree.addNode(new Node().setSubElement(tree.getNode(1)).setSubElement(tree.getNode(2)
                    .setID(5))
                    .setContent(person1));

        PersonGen person2 = new PersonGen();
        person2.setAttribute("name", "Peter");
        person2.setAttribute("ID", "2");
        tree.addNode(new Node().setSubElement(tree.getNode(3)).setSubElement(tree.getNode(4)
                    .setID(6))
                    .setContent(person2));
        //IDs still set by by XSD Collection

        //root element
        Gencompany company1 = new Gencompany();
        tree.addNode(new Node().setSubElement(tree.getNode(5)).setSubElement(tree.getNode(6))
                    .setID(7)
                    .setContent(company1));

    }
    //the end.
}
