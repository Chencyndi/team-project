package Usecase4Test;

import movieapp.entity.Comment;
import movieapp.entity.User;
import movieapp.entity.Watchlist;
import movieapp.use_case.comment.*;
import movieapp.use_case.common.UserDataAccessInterface;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostCommentInteractorTest {

    @Test
    void execute_addsTopLevelComment() {
        final InMemoryCommentDAO commentDAO = new InMemoryCommentDAO();
        final InMemoryUserDAO userDAO = new InMemoryUserDAO(new User("alice", "pass"));
        final PresenterStub presenter = new PresenterStub();
        final PostCommentInteractor interactor = new PostCommentInteractor(commentDAO, userDAO, presenter);

        final PostCommentInputData input = new PostCommentInputData(
                "comment-1",
                "alice",
                "  Great movie!  ",
                42
        );

        final PostCommentOutputData result = interactor.execute(input);

        assertNotNull(result);
        assertEquals("comment-1", result.getCommentId());
        assertEquals("alice", result.getCommenter());
        assertEquals("Great movie!", result.getText());

        assertNotNull(commentDAO.addedComment);
        assertEquals("comment-1", commentDAO.addedComment.getCommentID());
        assertEquals("alice", commentDAO.addedComment.getCommenter().getUsername());
        assertEquals("Great movie!", commentDAO.addedComment.getText());
        assertEquals(42, commentDAO.addedComment.getMovieID());
        assertNull(commentDAO.repliedComment);

        assertSame(result, presenter.successData);
    }

    @Test
    void execute_addsReply() {
        final InMemoryCommentDAO commentDAO = new InMemoryCommentDAO();
        final InMemoryUserDAO userDAO = new InMemoryUserDAO(new User("bob", "secret"));
        final PresenterStub presenter = new PresenterStub();
        final PostCommentInteractor interactor = new PostCommentInteractor(commentDAO, userDAO, presenter);

        final PostCommentInputData input = new PostCommentInputData(
                null,
                "bob",
                "Replying right now",
                99,
                "parent-123"
        );

        final PostCommentOutputData result = interactor.execute(input);

        assertNotNull(result);
        assertEquals("bob", result.getCommenter());
        assertEquals("Replying right now", result.getText());

        assertNull(commentDAO.addedComment);
        assertNotNull(commentDAO.repliedComment);
        assertEquals("Replying right now", commentDAO.repliedComment.getText());
        assertEquals("bob", commentDAO.repliedComment.getCommenter().getUsername());
        assertEquals(99, commentDAO.repliedComment.getMovieID());
        assertEquals("parent-123", commentDAO.replyParentId);
        assertEquals(result.getCommentId(), commentDAO.repliedComment.getCommentID());

        assertSame(result, presenter.successData);
    }

    private static final class InMemoryCommentDAO implements CommentDataAccessInterface {
        private Comment addedComment;
        private Comment repliedComment;
        private String replyParentId;

        @Override
        public List<Comment> getComments(Integer movieID) {
            return new ArrayList<>();
        }

        @Override
        public void addComment(Comment comment) {
            this.addedComment = comment;
        }

        @Override
        public void addReply(Comment reply, String parentCommentID) {
            this.repliedComment = reply;
            this.replyParentId = parentCommentID;
        }
    }

    private static final class InMemoryUserDAO implements UserDataAccessInterface {
        private final User user;

        private InMemoryUserDAO(User user) {
            this.user = user;
        }

        @Override
        public boolean existsByUsername(String username) {
            return user != null && user.getUsername().equals(username);
        }

        @Override
        public void addUser(User user) {
            throw new UnsupportedOperationException("Not required for tests");
        }

        @Override
        public User findByUsername(String username) {
            return existsByUsername(username) ? user : null;
        }

        @Override
        public Watchlist getWatchlist(String username) {
            return null;
        }

        @Override
        public void addWatchlist(Integer movieID, String username) {
            throw new UnsupportedOperationException("Not required for tests");
        }

        @Override
        public void removeWatchlist(Integer movieID, String username) {
            throw new UnsupportedOperationException("Not required for tests");
        }

        @Override
        public Integer getMovieRating(String username, Integer movieID) {
            return null;
        }

        @Override
        public void addRating(Integer movieID, String username, Integer rating) {
            throw new UnsupportedOperationException("Not required for tests");
        }

        @Override
        public void removeRating(Integer movieID, String username) {
            throw new UnsupportedOperationException("Not required for tests");
        }
    }

    private static final class PresenterStub implements PostCommentOutputBoundary {
        private PostCommentOutputData successData;

        @Override
        public PostCommentOutputData presentValidationError(String errorMessage) {
            throw new AssertionError("Unexpected validation error: " + errorMessage);
        }

        @Override
        public PostCommentOutputData presentUserNotFound(String errorMessage) {
            throw new AssertionError("Unexpected missing user: " + errorMessage);
        }

        @Override
        public PostCommentOutputData presentSuccess(PostCommentOutputData outputData) {
            this.successData = outputData;
            return outputData;
        }
    }
}

