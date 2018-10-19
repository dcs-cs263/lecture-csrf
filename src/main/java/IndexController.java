package dcs;

import spark.*;
import java.util.*;

import static dcs.SessionUtil.*;

public class IndexController {

    // Serve the index page (GET request)
    public static Route serveIndexPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();

        model.put("news", LoginController.database.getNews());

        return ViewUtil.render(request, model, "/velocity/index.vm");
    };

    public static Route serveAddNewsPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, "/velocity/addnews.vm");
    };

    public static Route handleAddNews = (Request request, Response response) -> {

        String story = request.queryParams("story");
        DCSNews news = new DCSNews(story);
        LoginController.database.addNews(news);

        // otherwise just redirect the user to the index
        response.redirect("/");
        return null;
    };
}
