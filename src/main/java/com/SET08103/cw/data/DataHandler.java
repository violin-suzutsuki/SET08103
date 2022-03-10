package com.SET08103.cw.data;

import com.SET08103.cw.objects.City;
import com.SET08103.cw.objects.Continent;
import com.SET08103.cw.objects.Country;
import com.SET08103.cw.objects.Region;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DataHandler.java
 *
 * This is the class that handles communication with the backend database.
 */
public final class DataHandler
{
    private static DataHandler INSTANCE;

    private String CONNECTION_STRING = "jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true";
    private String USER = "root";
    private String PASSWORD = "example";

    private Connection connection;
    private List<Continent> continents;

    public static DataHandler getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new DataHandler();
        }

        return INSTANCE;
    }

    /**
     * Attempts to establish a connection with the supplied database.
     *
     * @param retryNumber number of times to attempt the connection
     * @return boolean depending on if connection was successful
     */
    public boolean connect(int retryNumber)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            return false;
        }

        for (int idx = 0; idx < retryNumber; idx++)
        {
            try
            {
                Thread.sleep(1000);
                connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
            }
            catch (SQLException | InterruptedException e)
            {
                System.out.println("[-] Unable to connect to SQL database, retrying...");
            }

            if (connection != null)
                return true;
        }

        return false;
    }

    /**
     * Gets the list of continent objects that are in memory.
     *
     * @return List of continents
     */
    public List<Continent> getContinents()
    {
        return continents;
    }

    /**
     * Gets a city record based on its id.
     *
     * @param id id to search for
     * @return City record containing city information, or null if not found
     */
    private City getCityFromId(int id)
    {
        try
        {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM city WHERE ID = ?");
            query.setInt(1, id);

            ResultSet results = query.executeQuery();

            if (results.next()) {
                City city = new City(
                        results.getInt("ID"),
                        results.getString("Name"),
                        results.getString("CountryCode"),
                        results.getString("District"),
                        results.getLong("Population")
                );

                return city;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Converts the results of an SQL query to an SQL object.
     *
     * @param results ResultSet from SQL query
     * @return Country object
     */
    private Country loadCountryFromResult(ResultSet results)
    {
        try {
            Country country = new Country(
                    results.getString("Code"),
                    results.getString("Name"),
                    results.getString("Continent"),
                    results.getString("Region"),
                    results.getLong("Population"),
                    getCityFromId(results.getInt("Capital"))
            );

            return country;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets a list of all countries in the world.
     *
     * @return List containing all countries in the world.
     */
    private List<Country> getAllCountries()
    {
        List<Country> countries = new ArrayList<Country>();

        try
        {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM country");
            ResultSet results = query.executeQuery();

            while (results.next())
            {
                Country country = loadCountryFromResult(results);

                if (country != null) {
                    countries.add(country);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return countries;
    }

    /**
     * Searches for all the countries in a given region.
     *
     * @param regionName region name to get countries for
     * @return List containing all countries in a given region
     */
    private List<Country> getCountriesInRegion(String regionName)
    {
        List<Country> countries = new ArrayList<Country>();

        try
        {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM country WHERE region = ?");
            query.setString(1, regionName);

            ResultSet results = query.executeQuery();

            while (results.next())
            {
                Country country = loadCountryFromResult(results);

                if (country != null) {
                    countries.add(country);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return countries;
    }

    /**
     * Searches for all the regions in a given continent.
     *
     * @param continent continent name to get regions for
     * @return List containing all regions in a given continent
     */
    private List<Region> getRegionsInContinent(String continent)
    {
        List<Region> regions = new ArrayList<Region>();

        try
        {
            PreparedStatement query = connection.prepareStatement("SELECT DISTINCT region FROM country WHERE continent = ?");
            query.setString(1, continent);

            ResultSet results = query.executeQuery();

            while (results.next())
            {
                String region = results.getString("region");

                Region regionObj = new Region(region);
                regionObj.addCountries(getCountriesInRegion(region));

                regions.add(regionObj);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return regions;
    }

    /**
     * Populates the continents list with continents data from the SQL database
     *
     * @return void
     */
    public void loadContinents()
    {
        if (continents == null)
        {
            return;
        }

        continents = new ArrayList<Continent>();

        try
        {
            PreparedStatement query = connection.prepareStatement("SELECT DISTINCT continent FROM country");
            ResultSet results = query.executeQuery();

            while (results.next())
            {
                String continent = results.getString("continent");

                Continent continentObj = new Continent(continent);
                continentObj.addRegions(getRegionsInContinent(continent));

                continents.add(continentObj);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}