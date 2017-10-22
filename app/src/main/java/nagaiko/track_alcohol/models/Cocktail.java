package nagaiko.track_alcohol.models;

import com.google.gson.annotations.SerializedName;

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
        public String name;
        public String measure;

        public Ingredient(String name, String measure) {
            this.name = name;
            this.measure = measure;
        }
    }

    /*
        id of drink in db
     */
    @SerializedName("idDrink")
    public int id;

    /*
        name of drink
     */
    @SerializedName("strDrink")
    public String name;

    /*
        category of drink
     */
    @SerializedName("strCategory")
    public String categoryName;

    /*
        TODO: mode understand what it.
        I think, it is the category of drink in International Beer Association
     */
    @SerializedName("strIBA")
    public String IBA;

    /*
        Type of Alcoholic of drink. May be one of these:
            Alcoholic, Non alcoholic, Optional alcohol, null
     */
    @SerializedName("strAlcoholic")
    public String alcoholic;

    /*
        Type of glass, which used to drink it.
        One of these: Highball glass, Cocktail glass, eth
        Full list available on http://www.thecocktaildb.com/api/json/v1/1/list.php?g=list
     */
    @SerializedName("strGlass")
    public String glass;

    /*
        Instruction how to prepare drink.
     */
    @SerializedName("strInstructions")
    public String instruction;

    /*
        Image of drink.
     */
    @SerializedName("strDrinkThumb")
    public String thumb;


    /*
        List of ingredients and its measures.
     */
    @SerializedName("strIngredient1")
    protected String ingredient1;

    @SerializedName("strMeasure1")
    protected String measure1;

    @SerializedName("strIngredient2")
    protected String ingredient2;

    @SerializedName("strMeasure2")
    protected String measure2;

    @SerializedName("strIngredient3")
    protected String ingredient3;

    @SerializedName("strMeasure3")
    protected String measure3;

    @SerializedName("strIngredient4")
    protected String ingredient4;

    @SerializedName("strMeasure4")
    protected String measure4;

    @SerializedName("strIngredient5")
    protected String ingredient5;

    @SerializedName("strMeasure5")
    protected String measure5;

    @SerializedName("strIngredient6")
    protected String ingredient6;

    @SerializedName("strMeasure6")
    protected String measure6;

    @SerializedName("strIngredient7")
    protected String ingredient7;

    @SerializedName("strMeasure7")
    protected String measure7;

    @SerializedName("strIngredient8")
    protected String ingredient8;

    @SerializedName("strMeasure8")
    protected String measure8;

    @SerializedName("strIngredient9")
    protected String ingredient9;

    @SerializedName("strMeasure9")
    protected String measure9;

    @SerializedName("strIngredient10")
    protected String ingredient10;

    @SerializedName("strMeasure10")
    protected String measure10;

    @SerializedName("strIngredient11")
    protected String ingredient11;

    @SerializedName("strMeasure11")
    protected String measure11;

    @SerializedName("strIngredient12")
    protected String ingredient12;

    @SerializedName("strMeasure12")
    protected String measure12;

    @SerializedName("strIngredient13")
    protected String ingredient13;

    @SerializedName("strMeasure13")
    protected String measure13;

    @SerializedName("strIngredient14")
    protected String ingredient14;

    @SerializedName("strMeasure14")
    protected String measure14;

    @SerializedName("strIngredient15")
    protected String ingredient15;

    @SerializedName("strMeasure15")
    protected String measure15;

    /*
        List of ingredients in array.
        It can be more comfortable to use in program.
     */
    private Ingredient[] _ingredients = null;

    /*
        Return list of ingredients by array.
     */
    public Ingredient[] getIngredients() {
        if (_ingredients == null) {
            _ingredients = new Ingredient[15];
            if (!"".equals(ingredient1)) {
                _ingredients[0] = new Ingredient(ingredient1, measure1);
            }
            if (!"".equals(ingredient2)) {
                _ingredients[1] = new Ingredient(ingredient2, measure2);
            }
            if (!"".equals(ingredient3)) {
                _ingredients[2] = new Ingredient(ingredient3, measure3);
            }
            if (!"".equals(ingredient4)) {
                _ingredients[3] = new Ingredient(ingredient4, measure4);
            }
            if (!"".equals(ingredient5)) {
                _ingredients[4] = new Ingredient(ingredient5, measure5);
            }
            if (!"".equals(ingredient6)) {
                _ingredients[5] = new Ingredient(ingredient6, measure6);
            }
            if (!"".equals(ingredient7)) {
                _ingredients[6] = new Ingredient(ingredient7, measure7);
            }
            if (!"".equals(ingredient8)) {
                _ingredients[7] = new Ingredient(ingredient8, measure8);
            }
            if (!"".equals(ingredient9)) {
                _ingredients[8] = new Ingredient(ingredient9, measure9);
            }
            if (!"".equals(ingredient10)) {
                _ingredients[9] = new Ingredient(ingredient10, measure10);
            }
            if (!"".equals(ingredient11)) {
                _ingredients[10] = new Ingredient(ingredient11, measure11);
            }
            if (!"".equals(ingredient12)) {
                _ingredients[11] = new Ingredient(ingredient12, measure12);
            }
            if (!"".equals(ingredient13)) {
                _ingredients[12] = new Ingredient(ingredient13, measure13);
            }
            if (!"".equals(ingredient14)) {
                _ingredients[13] = new Ingredient(ingredient14, measure14);
            }
            if (!"".equals(ingredient15)) {
                _ingredients[14] = new Ingredient(ingredient15, measure15);
            }
        }
        return _ingredients;
    }
}
