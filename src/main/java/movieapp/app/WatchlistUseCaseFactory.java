package movieapp.app;// Application.java

import movieapp.data_access.DatabaseWatchlistDAO;
import movieapp.interface_adapter.watchlist.*;
import movieapp.use_case.watchlist.*;
import movieapp.view.WatchlistView;

public class WatchlistUseCaseFactory {

    private WatchlistUseCaseFactory() {
    }

    public static WatchlistView create() {
        // Use the simple in-memory version
        final DatabaseWatchlistDAO dataAccess = new DatabaseWatchlistDAO();

        final WatchlistViewModel viewModel = new WatchlistViewModel();
        final WatchlistPresenter presenter = new WatchlistPresenter(viewModel);

        final AddToWatchlistInteractor addInteractor =
                new AddToWatchlistInteractor(dataAccess, presenter);
        final RemoveFromWatchlistInteractor removeInteractor =
                new RemoveFromWatchlistInteractor(dataAccess, presenter);
        final ViewWatchlistInteractor viewInteractor =
                new ViewWatchlistInteractor(dataAccess, presenter);

        final WatchlistController controller = new WatchlistController(
                addInteractor, removeInteractor, viewInteractor
        );

        return new WatchlistView(viewModel, controller);
    }

    /**
     * Creates just the controller (useful for adding watchlist functionality to other views).
     * @param dataAccess the watchlist data access object
     * @param viewModel the view model to update
     * @return the configured WatchlistController
     */
    public static WatchlistController createController(
            DatabaseWatchlistDAO dataAccess,
            WatchlistViewModel viewModel) {

        final WatchlistPresenter presenter = new WatchlistPresenter(viewModel);

        final AddToWatchlistInteractor addInteractor =
                new AddToWatchlistInteractor(dataAccess, presenter);
        final RemoveFromWatchlistInteractor removeInteractor =
                new RemoveFromWatchlistInteractor(dataAccess, presenter);
        final ViewWatchlistInteractor viewInteractor =
                new ViewWatchlistInteractor(dataAccess, presenter);

        return new WatchlistController(addInteractor, removeInteractor, viewInteractor);
    }
}