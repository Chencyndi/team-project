package movieapp.interface_adapter.watchlist;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View Model for the Watchlist view.
 * Manages the state and notifies observers of changes.
 */
public class WatchlistViewModel {

    public static final String TITLE = "Watchlist";
    public static final String WATCHLIST_UPDATED = "watchlistUpdated";

    private WatchlistState state = new WatchlistState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public WatchlistState getState() {
        return state;
    }

    public void setState(WatchlistState state) {
        this.state = state;
    }

    public void firePropertyChanged() {
        support.firePropertyChange(WATCHLIST_UPDATED, null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}