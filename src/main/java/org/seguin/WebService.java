package org.seguin;

import com.google.gson.Gson;
import org.seguin.Exceptions.*;
import org.seguin.Model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by 1507779 on 2017-10-02.
 */
@Path("/") //recoit tout dant /api/...
public class WebService {

    static ArrayList<Users> userList = new ArrayList<Users>();
    static ArrayList<Drinks> drinkList = new ArrayList<Drinks>();
    static IngredientList allIngredients = new IngredientList();
    static ArrayList<Ingredients> ingredientList = new ArrayList<Ingredients>(allIngredients);
    public static final String Cookie="Drinks_R_Uscookie";

    //region User Related

    @GET @Path("/Users/GetUser/{id}")
    public Users getUserById(@PathParam("id") int id){
        System.out.println("Get getuserbyid id: " + id);

        for (Users user :
                userList) {
            if (id == user.getId()){
                return user;
            }
        }
        throw new UserNotFoundException();
        //return null;

    }

    @GET @Path("/Users/Logout")
    public Response logoutUser(@CookieParam(Cookie) Cookie cookie){
        System.out.println("Get logoutuser id: ");
        NewCookie cookieasupprimer = new NewCookie(Cookie, null, "/", null, null, 0, true);
        return Response.ok(new Gson().toJson(true), MediaType.APPLICATION_JSON).cookie(cookieasupprimer).build();
    }

    @GET @Path("/Users/MyDrinks/{id}")
    public ArrayList<Drinks> getMyDrinks(@PathParam("id") int id){
        System.out.println("Get getmydrinks id: " + id);

        Users user = getUserById(id);
        ArrayList<Drinks> drinks = user.getMyDrinks();
        return drinks;
    }

    @GET @Path("/Users/MyFavs/{id}")
    public ArrayList<Drinks> getMyFavs(@PathParam("id") int id){
        System.out.println("Get getmyfavs id: " + id);

        Users user = getUserById(id);
        ArrayList<Drinks> favs = user.getMyFavs();
        return favs;
    }

    @POST
    @Path("/Users/AddDrink")
    public String addMyDrinks(UserDrink drink){
        System.out.println("POST addmydrinks drink: " + drink.drink.getName() + " userid: " + drink.user.getId());

        Users user = getUserById(drink.user.getId());
        user.addMyDrink(drink.drink);
        return "ok";
    }

    @POST @Path("/Users/AddFav")
    public String addMyFavs(UserDrink fav){
        System.out.println("POST addMyFavs drink: " + fav.drink + " userid: " + fav.user.getId());

        Users user = getUserById(fav.user.getId());
        user.addMyFav(fav.drink);
        return "ok";
    }

    @POST @Path("/Users/VerifyCredentials")
    public Response verifyCredentials(EmailPassword login){
        System.out.println("POST verifyCredentials emailpassword: " + login.email + " " + login.password);
        for (Users user :
                userList) {
            if (user.getUsername().equals(login.email)){
                if (user.getPassword().equals(login.password)){
                    Token token = new Token();
                    token.UserId = UUID.randomUUID().toString();
                    token.TokenId = UUID.randomUUID().toString();
                    NewCookie cookie = new NewCookie(Cookie, token.TokenId, "/", null, "id token", 64800, true);
                    return Response.ok(new Gson().toJson(token), MediaType.APPLICATION_JSON).cookie(cookie).build();
                }
            }
        }
        throw new BadLoginExecption();
        //return null;
    }

    @POST @Path("/Users/Create")
    public Users createUser(EmailPassword credentials){
        System.out.println("POST createuser emailpassword: " + credentials.email + " " + credentials.password);

        for (Users user:
             userList) {
            if (user.getUsername().equals(credentials.email)){
                throw new UsernameExistsException();
                //return null;
            }
        }
        Users user = new Users(credentials.email, credentials.password);
        userList.add(user);
        return user;

    }

    //endregion

    //region Drink Related

    @POST @Path("/Drinks/Create")
    public String createDrink(Drinks drink){
        System.out.println("POST createdrink drink: " + drink.getName());

        drinkList.add(drink);
        return "ok";
    }

    @GET @Path("/Drinks/GetAllDrinks")
    public ArrayList<Drinks> getAllDrinks(){
        System.out.println("GET getalldrink ");

        return drinkList;
    }

    //Utiliser dans l'outil de recherche a venir
    /*
    @GET @Path("/Drinks/GetDrinkByName/{name}")
    public ArrayList<Drinks> getDrinkByName(@PathParam("name") String name){
        System.out.println("GET getdrinkbyname name: " + name);

        ArrayList<Drinks> list = new ArrayList<Drinks>();

        return list;
    }
    */

    /*
    @GET @Path("/Drinks/GetDrinkById/{id}")
    public Drinks getDrinkById(@PathParam("id") int id){
        System.out.println("GET getdrinkbyid id: " + id );

        for (Drinks drink :
                drinkList) {
            if (drink.getId() == id){
                return drink;
            }
        }
        throw new DrinkNotFoundException();
    }
    */

    //endregion

    //region Ingredient Related

    @POST @Path("/Ingredients/Create")
    public String createIngredient(Ingredients ingr){
        System.out.println("POST createingredient ingredient: " + ingr.Nom);

        ingredientList.add(ingr);
        return "ok";
    }

    /*
    @GET @Path("/Ingredients/GetIngredientByName/{name}")
    public Ingredients getIngredientByName(@PathParam("name") String name){
        System.out.println("GET getingredientbyname name: " + name );

        for (Ingredients ingr :
                ingredientList) {
            if (ingr.Nom.equals(name)){
                return ingr;
            }
        }
        throw new IngredientNotFoundException();
    }
    */

    /*
    @GET @Path("/Ingredients/GetingredientById/{id}")
    public Ingredients getIngredientById(@PathParam("id") int id){
        System.out.println("GET getingredientbyid id: " + id);

        for (Ingredients ingr :
                ingredientList) {
            if (ingr.Id == id){
                return ingr;
            }
        }
        throw new IngredientNotFoundException();
    }
    */

    @GET @Path("/Ingredients/GetAllIngredient")
    public ArrayList<Ingredients> getAllIngredients(){
        System.out.println("GET getallingredients");
        return ingredientList;
    }

    //endregion

}
