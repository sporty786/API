package com.example;

import java.util.List;

public class PaintingService {
    public PaintingDAO paintingDAO;

    public PaintingService(){
        this.paintingDAO = new PaintingDAO();
    }

    public List<Painting> getAllPaintings(){
        return paintingDAO.getAllPaintings();
    }

    public List<Painting> getAllPaintingsMadeInYear(int year){
        return paintingDAO.getAllPaintingsMadeInYear(year);
    }

    public int getOldestPaintingYear(){
        return paintingDAO.getOldestPaintingYear();
    }

    public Painting insertPainting(Painting p){
        paintingDAO.insertPainting(p);
        return p;
    }
}
