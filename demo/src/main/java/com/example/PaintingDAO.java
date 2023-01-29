package com.example;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/** 
 * DAO (data-access-object) is a style of a class that is designed to handle database interactions
 */

public class PaintingDAO {

    Connection conn;

    public PaintingDAO(){
        try{
            conn = DriverManager.getConnection("jdbc:h2:./h2/db");
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * for testing - can drop my painting table
     */
    public void dropPaintingTable(){
        try{
            PreparedStatement ps = conn.prepareStatement("DROP TABLE painting;");
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * for testing - i can create my painting table
     */
    public void createPaintingTable(){
        try{
            PreparedStatement ps = conn.prepareStatement("CREATE TABLE painting(title varchar(255), year_made int);");
            ps.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /** 
     * JDBC logic to insert a painting into my h2 database
     * @param painting
     */
    public void insertPainting(Painting painting){
        try{
            PreparedStatement ps = conn.prepareStatement("INSERT INTO painting(title, year_made) VALUES (?, ?)");
            // ps.setString, ps.setInt 'fills in' the question marks in a preparedStatement
            // this not only makes them easier to write but it also avoids a security issue called SQL injection
            ps.setString(1, painting.title);
            ps.setInt(2, painting.year_made);
            ps.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * JDBC logic to get all my paintings from painting table
     * @return List<Painting>
     */
    public List<Painting> getAllPaintings(){
        try{
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM painting;");
            // Because we want to retrieve data meaningfully in java, we have to expect data in the form of a 'resultset'
            // We also have to use executeQuery instead of executeUpdate, because executeQuery is expecting a resultSet.
            ResultSet rs = ps.executeQuery();
            List<Painting> allPaintings = new ArrayList<>();
            // We have to loop through the entire resultset for very item it contains
            while(rs.next()){
                // We have to extract the DV column of each row into a meaningful java object
                Painting newPainting = new Painting(rs.getString("title"), rs.getInt("year_made"));
                allPaintings.add(newPainting);
            }
            return allPaintings;
        } catch (SQLException e){
            e.printStackTrace();
        }
        // In the event that we don't get to return allPaintings because a SQLException was thrown, just return null
        return null;
    }

    /**
     * Of all the paintings in the painting table, return the oldest painting's year.
     * 
     * @return select min(year) as oldest_year from painting;
     */
    public int getOldestPaintingYear(){
        try{
            PreparedStatement ps = conn.prepareStatement("SELECT MIN(year_made) AS oldest_year FROM painting;");
            ResultSet rs = ps.executeQuery();
            // If result set has a value, do this
            // (If there is no result set, skip this block and return 0)
            if(rs.next()){
                int oldestYear = rs.getInt("oldest_year");
                return oldestYear;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * using the preparedstatement's setInt (or setString etc) methods to set unknown parameters of a SQL query.
     * @param year
     * @return
     */
    public List<Painting> getAllPaintingsMadeInYear(int year){
        try{
            // we could also write:
            // Prepared Statement ps = conn.prepareStatement("SELECT * FROM painting WHERE year_made = " + year + ";");
            // Above option is worse because can lead to convoluted broken string, and risks SQL injection
            // Using question marks is cleaner, and sanitizes input to make SQL injection impossible
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM painting WHERE year_made = ?;");
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            List<Painting> allPaintings = new ArrayList<>();
            // We have to loop through the entire resultset for very item it contains
            while(rs.next()){
                // We have to extract the DV column of each row into a meaningful java object
                Painting newPainting = new Painting(rs.getString("title"), rs.getInt("year_made"));
                allPaintings.add(newPainting);
            }
            return allPaintings;
        } catch (SQLException e){
            e.printStackTrace();
        }
        // In the event that we don't get to return allPaintings because a SQLException was thrown, just return null
        return null;
    }
}
