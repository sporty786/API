package com.example;

import io.javalin.Javalin;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;


public class PaintingController {
    Javalin app;
    public PaintingService paintingService;

    public PaintingController(){
        app = Javalin.create();
        paintingService = new PaintingService();
    }

    public void startAPI(){
        // endpoint that returns all paintings
        app.get("/painting", ctx -> {
            List<Painting> paintings = paintingService.getAllPaintings();
            ctx.json(paintings);
        });
        // endpoint that adds (POSTS) a new painting
        app.post("/painting", ctx -> {
            ObjectMapper om = new ObjectMapper();
            Painting painting = om.readValue(ctx.body(), Painting.class);
            Painting p = paintingService.insertPainting(painting);
            ctx.json(p);
        });
        app.get("/painting/year", ctx -> {
            // should be "year_made"? Not sure because http requests not working
            String year_input = ctx.pathParam("year");
            try {
                int year = Integer.parseInt(year_input);
                List<Painting> paintingsInYear = paintingService.getAllPaintingsMadeInYear(year);
                ctx.json(paintingsInYear);
            } catch (NumberFormatException e){
                ctx.status(400);
            }
        });
        app.start();

        /**
         * other http verbs:
         * get
         * post
         * delete
         * 
         * put (total update - replace everything in a particular item)
         * patch (partial update [one or two values])
         */

         /**
          * Today we talked about:
          * the three layer architecture
          * testing/mockito
          */
    }
}
