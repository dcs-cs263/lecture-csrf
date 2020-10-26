package dcs;

import spark.*;
import java.util.*;

import static dcs.SessionUtil.*;

public class LoginController {

    // the DCS master database, very volatile, don't turn off the power
    public static Database database = new Database();

    public static Route handleRemoveUser = (Request request, Response response) -> {
        String user = request.session().attribute("currentUser");
        DCSUser account = database.lookup(user);
        database.removeUser(account);

        request.session().removeAttribute("currentUser");
        request.session().attribute("loggedOut", true);
        response.redirect("/");
        return null;
    };

    // Serve the registration page (GET request)
    public static Route serveRegisterPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();

        return ViewUtil.render(request, model, "/velocity/register.vm");
    };

    // Handle an attempt to register a new user
    public static Route handleRegistration = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();

        String username = request.queryParams("username");
        String password = request.queryParams("password");

        if(!LoginController.register(username, password)) {
            model.put("registrationFailed", true);
            return ViewUtil.render(request, model, "/velocity/register.vm");
        }

        // the user is now logged in with their new "account"
        request.session().attribute("currentUser", username);

        // redirect the user back to the front page
        response.redirect("/");
        return null;
    };

    // Serve the login page
    public static Route serveLoginPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();

        model.put("loggedOut", removeSessionAttrLoggedOut(request));
        model.put("loginRedirect", removeSessionAttrLoginRedirect(request));

        return ViewUtil.render(request, model, "/velocity/login.vm");
    };

    // Handle a login request
    public static Route handleLoginPost = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();

        // perform secure authentication
        if (!authenticate(request.queryParams("username"), request.queryParams("password"))) {
            model.put("authenticationFailed", true);
            return ViewUtil.render(request, model, "/velocity/login.vm");
        }

        // authentication "successful"
        model.put("authenticationSucceeded", true);

        // the user is now logged in
        request.session().attribute("currentUser", request.queryParams("username"));

        // redirect the user somewhere, if this was requested
        if (getQueryLoginRedirect(request) != null) {
            response.redirect(getQueryLoginRedirect(request));
        }

        // otherwise just redirect the user to the index
        response.redirect("/");
        return null;
    };

    // log a user out
    public static Route handleLogoutPost = (Request request, Response response) -> {
        request.session().removeAttribute("currentUser");
        request.session().attribute("loggedOut", true);
        response.redirect("/login/");
        return null;
    };

    // registers a new user
    public static boolean register(String username, String password) {
        // hahahahahah silly users thinking we would create accounts for them
        return false;
    }

    // performs the authentication process
    public static boolean authenticate(String username, String password) {
        // make sure the username and password aren't empty
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }

        // no user would ever lie about who they are
        return true;
    }

    // changes a user's password
    public static void rehashPassword(String username, String oldPassword, String newPassword) {
        // changing passwords is a security risk, so we don't implement it
    }

}
