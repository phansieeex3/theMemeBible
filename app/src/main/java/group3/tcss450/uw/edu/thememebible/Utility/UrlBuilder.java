package group3.tcss450.uw.edu.thememebible.Utility;

/**
 * Static utility class to assist in building URLs for the MemeData AsyncTask.
 *
 * @author Peter Phe
 * @version 1.0
 */
public final class UrlBuilder {

    private static final String BASE_URL = "http://version1.api.memegenerator.net/";
    private static final String API_KEY = "7A81A4B0-C434-4DA2-B8D6-1A63E5D63400";
    private static final String USERNAME = "phansac";
    private static final String PASSWORD = "nomnomnom2";

    // prevent instantiation
    private UrlBuilder() {
        // intentionally empty
    }


    /**
     * Gets the URL for creating a new instance of a stock generator image. generatorID and imageID
     * fields from a Generators_Select_* API call must be used to successfully create an instance.
     *
     * @param theGeneratorID the generatorID from a Generators_Select_* API call
     * @param theImageID the imageID from a Generators_Select_* API call
     * @param theTopText the desired caption for the top text (can be omitted)
     * @param theBottomText the desired caption for the bottom text (can be omitted)
     * @return the complete Instance_Create URL
     *
     * ex. http://version1.api.memegenerator.net/Instance_Create?username=test8&password=test8
     * &languageCode=en&generatorID=45&imageID=20&text0=push%20a%20hipster%20down%20the%20stairs
     * &text1=now%20look%20who%27s%20tumbling&apiKey=demo
     */
    public static String getInstanceCreateUrl(int theGeneratorID, int theImageID,
                                              String theTopText, String theBottomText) {
        return BASE_URL + "Instance_Create?" + addCredentials() + "&langageCode=en&generatorID="
                + theGeneratorID + "imageID=" + theImageID + "&text0=" + theTopText + "&text1="
                + theBottomText + "&" + addAPIKey();
    }

    /**
     * Gets the URL of an instance by instanceID.
     *
     * @param theInstanceID the instanceID to retrieve
     * @return the complete Instance_Select URL
     *
     * ex. http://version1.api.memegenerator.net/Instance_Select?instanceID=72628355&apiKey=demo
     */
    public static String getInstanceSelectUrl(int theInstanceID) {
        return BASE_URL + "Instance_Select?instanceID=" + theInstanceID + addAPIKey();
    }

    /**
     * Gets the URL for searching instances by keyword(s).
     *
     * @param theQuery the instances to search for
     * @return the complete Instances_Search URL
     *
     * ex. http://version1.api.memegenerator.net/Instances_Search?q=insanity&pageIndex=0
     * &pageSize=12&apiKey=demo
     */
    public static String getInstancesSearchUrl(String theQuery) {
        return BASE_URL + "Instances_Search?q=" + theQuery + "&pageIndex=0&pageSize=12&" + addAPIKey();
    }

    /**
     * Gets the URL for new instances created by a specified urlName. If null, gets all.
     *
     * @param theUrlName the urlName to search for
     * @return the complete Instances_Select_ByNew URL
     *
     * ex. http://version1.api.memegenerator.net/Instances_Select_ByNew?languageCode=en&pageIndex=0
     * &urlName=Insanity-Wolf&apiKey=demo
     */
    public static String getInstancesSelectByNewUrl(String theUrlName) {
        String url = BASE_URL + "Instances_Select_ByNew?languageCode=en&pageIndex=0&urlName=";
        if (theUrlName.length() > 0) { // if valid urlName, selects those created using the urlName
            url += theUrlName;
        } else { // selects most recent from all, not just those created by using the urlName
            url += "null";
        }

        return url + "&" + addAPIKey();
    }

    /**
     * Gets the URL for the most popular instances specified by a urlName. If null, gets all.
     *
     * @param theUrlName the urlName to search for
     * @return the complete Instances_Select_ByPopular URL
     *
     * ex. http://version1.api.memegenerator.net/Instances_Select_ByPopular?languageCode=en
     * &pageIndex=0&urlName=Insanity-Wolf&days=&apiKey=demo
     */
    public static String getInstancesSelectByPopularUrl(String theUrlName) {
        String url = BASE_URL + "Instances_Select_ByPopular?languageCode=en&pageIndex=0&urlName=";
        if (theUrlName.length() > 0) { // if valid urlName, selects those created using the urlName
            url += theUrlName;
        } else { // selects most popular from all, not just those created by using the urlName
            url += "null";
        }

        return url + "&days=&" + addAPIKey(); // days parameter can be specified later if we want
    }

    /**
     * Gets the newest Generator images.
     *
     * @return the complete Generators_Select_ByNew URL
     *
     * ex. http://version1.api.memegenerator.net/Generators_Select_ByNew?pageIndex=0&pageSize=12
     * &apiKey=demo
     */
    public static String getGeneratorsSelectByNewUrl() {
        return BASE_URL + "Generators_Select_ByNew?pageIndex=0&pageSize=12&" + addAPIKey();
    }

    /**
     * Gets the most popular Generator images.
     *
     * @return the complete Generators_Select_ByPopular URL
     *
     * ex. http://version1.api.memegenerator.net/Generators_Select_ByPopular?pageIndex=0
     * &pageSize=12&days=&apiKey=demo
     */
    public static String getGeneratorsSelectByPopularUrl() { // days purposely omitted (can add later)
        return BASE_URL + "Generators_Select_ByPopular?pageIndex=0&pageSize=12&days=&" + addAPIKey();
    }


    /**
     * Gets the generators most recently captioned.
     *
     * @return the complete Generators_Select_ByRecentlyCaptioned URL
     *
     * ex. http://version1.api.memegenerator.net/Generators_Select_ByRecentlyCaptioned?username=test8
     * &password=test8&apiKey=demo
     */
    public static String getGeneratorsSelectByRecentlyCaptionedUrl() {
        return BASE_URL + "Generators_Select_ByRecentlyCaptioned?" + addCredentials() + "&" + addAPIKey();
    }

    /**
     * Gets the trending Generator images.
     *
     * @return the complete Generators_Select_ByTrending URL
     *
     * ex. http://version1.api.memegenerator.net/Generators_Select_ByTrending
     */
    public static String getGeneratorsSelectByTrendingUrl() {
        return BASE_URL + "Generators_Select_ByTrending";
    }

    /**
     * Helper method to append the API key.
     * @return string with API key
     */
    private static String addAPIKey() {
        return "apiKey=" + API_KEY;
    }

    /**
     * Helper method to append API username/password credentials.
     * @return string with API login information
     */
    private static String addCredentials() {
        return "username=" + USERNAME + "&password=" + PASSWORD;
    }
}
