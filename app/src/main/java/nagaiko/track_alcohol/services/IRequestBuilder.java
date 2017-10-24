package nagaiko.track_alcohol.services;

/**
 * Created by altair on 24.10.17.
 */

public interface IRequestBuilder {
    IRequestBuilder setCocktailID(int cocktailID);
    IRequestBuilder setSearchMethod(String cocktailName, String ingredientName);
    IRequestBuilder setRandomMethod();
    IRequestBuilder setFilterMethod(String ingredientName, String alcoholic,
                                               String category, String glass);
    IRequestBuilder setListMethod(int listType);

    IRequest build();
}
