package movieapp.use_case.movielist;

/**
 * Sort types for a movie list.
 */
public enum SortType {
    ALPHABETICAL_AZ,
    ALPHABETICAL_ZA,
    RATING_DESC,
    RATING_ASC,
    POPULARITY_DESC,  // number of ratings (via popularity)
    POPULARITY_ASC,
    NONE
}
