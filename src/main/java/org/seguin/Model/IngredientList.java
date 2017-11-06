package org.seguin.Model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Gabriel on 25/10/2017.
 */
public class IngredientList extends ArrayList<Ingredients> {

    public IngredientList(){
        Ingredients ing1 = new Ingredients("Jus d'orange");
        Ingredients ing2 = new Ingredients("Coke");
        Ingredients ing3 = new Ingredients("Rum");
        Ingredients ing4 = new Ingredients("Whiskey");
        Ingredients ing5 = new Ingredients("Jus d'ananas");
        Ingredients ing6 = new Ingredients("Vodka");
        Ingredients ing7 = new Ingredients("Tequila");
        Ingredients ing8 = new Ingredients("Sprite");
        Ingredients ing9 = new Ingredients("Orangeade");
        Ingredients ing10 = new Ingredients("Bailey's");
        Ingredients ing11 = new Ingredients("Sour puss");
        Ingredients ing12 = new Ingredients("Amaretto");
        Ingredients ing13 = new Ingredients("Grand marnier");
        Ingredients ing14 = new Ingredients("Eau");
        Ingredients ing15 = new Ingredients("Thé Glacé");
        Ingredients ing16 = new Ingredients("Jus de citron");

        this.add(ing1);
        this.add(ing2);
        this.add(ing3);
        this.add(ing4);
        this.add(ing5);
        this.add(ing6);
        this.add(ing7);
        this.add(ing8);
        this.add(ing9);
        this.add(ing10);
        this.add(ing11);
        this.add(ing12);
        this.add(ing13);
        this.add(ing14);
        this.add(ing15);
        this.add(ing16);

        Collections.sort(this, new Comparator<Ingredients>() {
            public int compare(Ingredients o1, Ingredients o2) {
                return o1.Nom.compareToIgnoreCase(o2.Nom);
            }
        });

    }

}
