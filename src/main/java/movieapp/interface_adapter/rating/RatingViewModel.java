package movieapp.interface_adapter.rating;

import movieapp.interface_adapter.ViewModel;

public class RatingViewModel extends ViewModel<RatingState> {
    public static final String TITLE_LABEL = "Rate Movie";
    public static final String RATING_LABEL = "Your Rating:";
    public static final String SUBMIT_BUTTON_LABEL = "Submit Rating";
    public static final String REMOVE_BUTTON_LABEL = "Remove Rating";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    public RatingViewModel() {
        super("rate movie");
        setState(new RatingState());
    }
}