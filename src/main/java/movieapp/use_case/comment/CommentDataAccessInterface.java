package movieapp.use_case.comment;

import java.util.List;

import movieapp.entity.Comment;

public interface CommentDataAccessInterface {
    /**
     * Retrieves all comments associated with a specific movie.
     *
     * @param movieID the unique identifier of the movie.
     * @return a list of Comment objects associated with the movie.
     */
    List<Comment> getComments(Integer movieID);

    /**
     * Saves a new top-level comment to the data store.
     *
     * @param comment the comment entity to be saved.
     */
    void addComment(Comment comment);

    /**
     * Saves a reply to an existing comment.
     *
     * @param reply           the comment entity representing the reply.
     * @param parentCommentID the ID of the comment being replied to.
     */
    void addReply(Comment reply, String parentCommentID);

}
