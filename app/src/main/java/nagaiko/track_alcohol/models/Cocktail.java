package nagaiko.track_alcohol.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by altair on 20.10.17.
 */


/*
    Cocktail model. Represent data from API.
 */
public class Cocktail {

    /*
        Class of Ingredient.
        Contain name of ingredient and its measure.
     */
    public static class Ingredient {
        private String name;
        private String measure;

        public Ingredient(String name, String measure) {
            this.name = name;
            this.measure = measure;
        }

        public String getName(){
            return name;
        }

        public String getMeasure() {
            return measure;
        }


    }

    /*
        id of drink in db
     */
    @SerializedName("idDrink")
    private int id;

    /*
        name of drink
     */
    @SerializedName("strDrink")
    private String name;

    /*
        category of drink
     */
    @SerializedName("strCategory")
    private String categoryName;

    /*
        TODO: mode understand what it.
        I think, it is the category of drink in International Beer Association
     */
    @SerializedName("strIBA")
    private String IBA;

    /*
        Type of Alcoholic of drink. May be one of these:
            Alcoholic, Non alcoholic, Optional alcohol, null
     */
    @SerializedName("strAlcoholic")
    private String alcoholic;

    /*
        Type of glass, which used to drink it.
        One of these: Highball glass, Cocktail glass, eth
        Full list available on http://www.thecocktaildb.com/api/json/v1/1/list.php?g=list
     */
    @SerializedName("strGlass")
    private String glass;

    /*
        Instruction how to prepare drink.
     */
    @SerializedName("strInstructions")
    private String instruction;

    /*
        Image of drink.
     */
    @SerializedName("strDrinkThumb")
    private String thumb;


    /*
        List of ingredients and its measures.
     */
    @SerializedName("strIngredient1")
    private String ingredient1;

    @SerializedName("strMeasure1")
    private String measure1;

    @SerializedName("strIngredient2")
    private String ingredient2;

    @SerializedName("strMeasure2")
    private String measure2;

    @SerializedName("strIngredient3")
    private String ingredient3;

    @SerializedName("strMeasure3")
    private String measure3;

    @SerializedName("strIngredient4")
    private String ingredient4;

    @SerializedName("strMeasure4")
    private String measure4;

    @SerializedName("strIngredient5")
    private String ingredient5;

    @SerializedName("strMeasure5")
    private String measure5;

    @SerializedName("strIngredient6")
    private String ingredient6;

    @SerializedName("strMeasure6")
    private String measure6;

    @SerializedName("strIngredient7")
    private String ingredient7;

    @SerializedName("strMeasure7")
    private String measure7;

    @SerializedName("strIngredient8")
    private String ingredient8;

    @SerializedName("strMeasure8")
    private String measure8;

    @SerializedName("strIngredient9")
    private String ingredient9;

    @SerializedName("strMeasure9")
    private String measure9;

    @SerializedName("strIngredient10")
    private String ingredient10;

    @SerializedName("strMeasure10")
    private String measure10;

    @SerializedName("strIngredient11")
    private String ingredient11;

    @SerializedName("strMeasure11")
    private String measure11;

    @SerializedName("strIngredient12")
    private String ingredient12;

    @SerializedName("strMeasure12")
    private String measure12;

    @SerializedName("strIngredient13")
    private String ingredient13;

    @SerializedName("strMeasure13")
    private String measure13;

    @SerializedName("strIngredient14")
    private String ingredient14;

    @SerializedName("strMeasure14")
    private String measure14;

    @SerializedName("strIngredient15")
    private String ingredient15;

    @SerializedName("strMeasure15")
    private String measure15;

    /*
        List of ingredients in array.
        It can be more comfortable to use in program.
     */
    private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

    public Cocktail() {

    }

    public Cocktail(int id, String name, String categoryName, String IBA, String alcoholic, String glass,
                    String instruction, String thumb, ArrayList<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.categoryName = categoryName;
        this.IBA = IBA;
        this.alcoholic = alcoholic;
        this.glass = glass;
        this.instruction = instruction;
        this.thumb = thumb;
        this.ingredients = ingredients;
    }

    /*
                    Return list of ingredients by array.
                 */
    public ArrayList<Ingredient> getIngredients() {
        if (ingredients.isEmpty()) {
            if (!"".equals(ingredient1)) {
                ingredients.add(new Ingredient(ingredient1, measure1));
            }
            if (!"".equals(ingredient2)) {
                ingredients.add(new Ingredient(ingredient2, measure2));
            }
            if (!"".equals(ingredient3)) {
                ingredients.add(new Ingredient(ingredient3, measure3));
            }
            if (!"".equals(ingredient4)) {
                ingredients.add(new Ingredient(ingredient4, measure4));
            }
            if (!"".equals(ingredient5)) {
                ingredients.add(new Ingredient(ingredient5, measure5));
            }
            if (!"".equals(ingredient6)) {
                ingredients.add(new Ingredient(ingredient6, measure6));
            }
            if (!"".equals(ingredient7)) {
                ingredients.add(new Ingredient(ingredient7, measure7));
            }
            if (!"".equals(ingredient8)) {
                ingredients.add(new Ingredient(ingredient8, measure8));
            }
            if (!"".equals(ingredient9)) {
                ingredients.add(new Ingredient(ingredient9, measure9));
            }
            if (!"".equals(ingredient10)) {
                ingredients.add(new Ingredient(ingredient10, measure10));
            }
            if (!"".equals(ingredient11)) {
                ingredients.add(new Ingredient(ingredient11, measure11));
            }
            if (!"".equals(ingredient12)) {
                ingredients.add(new Ingredient(ingredient12, measure12));
            }
            if (!"".equals(ingredient13)) {
                ingredients.add(new Ingredient(ingredient13, measure13));
            }
            if (!"".equals(ingredient14)) {
                ingredients.add(new Ingredient(ingredient14, measure14));
            }
            if (!"".equals(ingredient15)) {
                ingredients.add(new Ingredient(ingredient15, measure15));
            }
        }
        return ingredients;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getIBA() {
        return IBA;
    }

    public String getAlcoholic() {
        return alcoholic;
    }

    public String getGlass() {
        return glass;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getThumb() {
        return thumb;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setIBA(String IBA) {
        this.IBA = IBA;
    }

    public void setAlcoholic(String alcoholic) {
        this.alcoholic = alcoholic;
    }

    public void setGlass(String glass) {
        this.glass = glass;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
