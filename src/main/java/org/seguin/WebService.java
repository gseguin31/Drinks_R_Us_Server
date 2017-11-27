package org.seguin;

import com.google.gson.Gson;
import org.seguin.Exceptions.*;
import org.seguin.Model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
     ArrayList<Token> tokenList = new ArrayList<Token>();
    //region User Related

    @GET @Path("/Users/GetUser/{id}")
    public Users getUserById(@CookieParam(Cookie) Cookie cookie, @PathParam("id") int id) throws InterruptedException {
        Thread.sleep(2000);
        if (!cookieIsValid(cookie))
            return null;

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

    @GET @Path("/Users/Logout/{id}")
    public Response logoutUser(@CookieParam(Cookie) Cookie cookie) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Get logoutuser id: ");
        for (Token tok :
                tokenList) {
            if (tok.TokenId.equals(cookie.getValue())){
                tokenList.remove(tok);
            }
        }

        NewCookie cookieasupprimer = new NewCookie(Cookie, null, "/", null, null, 0, true);
        return Response.ok(new Gson().toJson(true), MediaType.APPLICATION_JSON).cookie(cookieasupprimer).build();
    }

    @GET @Path("/Users/MyDrinks/{id}")
    public ArrayList<Drinks> getMyDrinks(@CookieParam(Cookie) Cookie cookie, @PathParam("id") int id) throws InterruptedException {
        Thread.sleep(2000);
        if (!cookieIsValid(cookie))
            return null;

        System.out.println("Get getmydrinks id: " + id);

        Users user = getUserById(cookie, id);
        ArrayList<Drinks> drinks = user.getMyDrinks();
        return drinks;
    }

    @GET @Path("/Users/MyFavs/{id}")
    public ArrayList<Drinks> getMyFavs(@CookieParam(Cookie) Cookie cookie, @PathParam("id") int id) throws InterruptedException {
        Thread.sleep(2000);
        if (!cookieIsValid(cookie))
            return null;

        System.out.println("Get getmyfavs id: " + id);

        Users user = getUserById(cookie, id);
        ArrayList<Drinks> favs = user.getMyFavs();
        return favs;
    }

    @POST
    @Path("/Users/AddDrink")
    public String addMyDrinks(@CookieParam(Cookie) Cookie cookie, UserDrink drink) throws InterruptedException {
        Thread.sleep(2000);
        if (!cookieIsValid(cookie))
            return null;

        System.out.println("POST addmydrinks drink: " + drink.drink.getName() + " userid: " + drink.user.getId());

        Users user = getUserById(cookie, drink.user.getId());
        user.addMyDrink(drink.drink);
        return "ok";
    }

    @POST @Path("/Users/AddFav")
    public String addMyFavs(@CookieParam(Cookie) Cookie cookie, UserDrink fav) throws InterruptedException {
        Thread.sleep(2000);
        if (!cookieIsValid(cookie))
            return null;

        System.out.println("POST addMyFavs drink: " + fav.drink + " userid: " + fav.user.getId());

        Users user = getUserById(cookie, fav.user.getId());
        user.addMyFav(fav.drink);
        return "ok";
    }

    @POST @Path("/Users/VerifyCredentials")
    public Response verifyCredentials(EmailPassword login) throws InterruptedException {
        Thread.sleep(2000);
        /*//testing
        Token token = new Token();
        token.UserId = Integer.toString(0);
        token.TokenId = UUID.randomUUID().toString();
        tokenList.add(token);
        NewCookie cookie = new NewCookie(Cookie, token.TokenId, "/", null, "id token", 64800, true);
        return Response.ok(new Gson().toJson(token), MediaType.APPLICATION_JSON).cookie(cookie).build();
        */
        System.out.println("POST verifyCredentials emailpassword: " + login.email + " " + login.password);
        for (Users user :
                userList) {
            if (user.getUsername().equals(login.email)){
                if (user.getPassword().equals(login.password)){
                    Token token = new Token();
                    token.UserId = Integer.toString(user.getId());
                    token.TokenId = UUID.randomUUID().toString();

                    //create expiration date for the token 15 days later from creation
                    token.dateExpiration = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(token.dateExpiration);
                    calendar.add(Calendar.DATE, 15);
                    token.dateExpiration = calendar.getTime();

                    tokenList.add(token);
                    NewCookie cookie = new NewCookie(Cookie, token.TokenId, "/", null, "id token", 64800, true);
                    return Response.ok(new Gson().toJson(token), MediaType.APPLICATION_JSON).cookie(cookie).build();
                }
            }
        }
        throw new BadLoginExecption();
        //return null;
    }

    @POST @Path("/Users/Create")
    public Response createUser(EmailPassword credentials) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("POST createUser emailpassword: " + credentials.email + " " + credentials.password);

        for (Users user:
                userList) {
            if (user.getUsername().equals(credentials.email)){
                throw new UsernameExistsException();
                //return null;
            }
        }
        Users user = new Users(credentials.email, credentials.password);
        userList.add(user);
        Token token = new Token();
        token.UserId = Integer.toString(user.getId());
        token.TokenId = UUID.randomUUID().toString();
        //create expiration date for the token 15 days later from creation
        token.dateExpiration = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(token.dateExpiration);
        calendar.add(Calendar.DATE, 15);
        token.dateExpiration = calendar.getTime();

        tokenList.add(token);
        NewCookie cookie = new NewCookie(Cookie, token.TokenId, "/", null, "id token", 64800, true);
        return Response.ok(new Gson().toJson(token), MediaType.APPLICATION_JSON).cookie(cookie).build();

    }

    //endregion

    //region Drink Related

    @POST @Path("/Drinks/Create")
    public String createDrink(@CookieParam(Cookie) Cookie cookie, Drinks drink) throws InterruptedException {
        Thread.sleep(2000);
        if (!cookieIsValid(cookie))
            return null;

        System.out.println("POST createdrink drink: " + drink.getName());

        drinkList.add(drink);
        return "ok";
    }

    @GET @Path("/Drinks/GetAllDrinks")
    public ArrayList<Drinks> getAllDrinks(@CookieParam(Cookie) Cookie cookie) throws InterruptedException {
        Thread.sleep(2000);
        if (!cookieIsValid(cookie))
            return null;

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
    public String createIngredient(@CookieParam(Cookie) Cookie cookie, Ingredients ingr) throws InterruptedException {
        Thread.sleep(2000);
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
    public ArrayList<Ingredients> getAllIngredients(@CookieParam(Cookie) Cookie cookie) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("GET getallingredients");
        return ingredientList;
    }

    //endregion

    private boolean cookieIsValid(Cookie cookie){
        for (Token tok :
                tokenList) {
            if (cookie.getValue().equals(tok.TokenId)) {
                if (tokenIsValid(tok))
                    return true;
                else{
                    tokenList.remove(tok);
                    return false;
                }

            }
        }
        return false;
    }

    private boolean tokenIsValid(Token token){
        if (token.dateExpiration.before(new Date()))
        return false;

        return true;
    }
}
