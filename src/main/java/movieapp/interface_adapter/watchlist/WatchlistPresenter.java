package movieapp.interface_adapter.watchlist;

import movieapp.use_case.watchlist.*;

public class WatchlistPresenter implements
        AddToWatchlistOutputBoundary,
        RemoveFromWatchlistOutputBoundary,
        ViewWatchlistOutputBoundary {

    private final WatchlistViewModel viewModel;

    public WatchlistPresenter(WatchlistViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // AddToWatchlist methods
    @Override
    public void prepareSuccessView(AddToWatchlistOutputData outputData) {
        final WatchlistState state = viewModel.getState();
        state.setMessage(outputData.getMessage());
        state.setError(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final WatchlistState state = viewModel.getState();
        state.setError(errorMessage);
        state.setMessage(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    // RemoveFromWatchlist methods
    public void prepareSuccessView(RemoveFromWatchlistOutputData outputData) {
        final WatchlistState state = viewModel.getState();
        state.setMessage(outputData.getMessage());
        state.setError(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    // ViewWatchlist methods
    public void prepareSuccessView(ViewWatchlistOutputData outputData) {
        final WatchlistState state = viewModel.getState();
        state.setMovies(outputData.getMovies());
        state.setError(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}