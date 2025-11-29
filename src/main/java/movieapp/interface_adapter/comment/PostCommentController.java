package movieapp.interface_adapter.comment;

import movieapp.use_case.comment.PostCommentInputBoundary;
import movieapp.use_case.comment.PostCommentInputData;
import movieapp.use_case.comment.PostCommentOutputData;

public class PostCommentController {
    private final PostCommentInputBoundary postCommentUseCase;

    public PostCommentController(PostCommentInputBoundary postCommentUseCase) {
        this.postCommentUseCase = postCommentUseCase;
    }

    /**
     * Executes the post comment use case for a new comment.
     *
     * @param commenter the username of the user posting the comment.
     * @param comment   the content/text of the comment.
     * @param movieID   the ID of the movie associated with the comment.
     * @return the output data returned by the use case.
     */
    public PostCommentOutputData execute(String commenter, String comment, Integer movieID) {
        final PostCommentInputData inputData = new PostCommentInputData(null, commenter, comment, movieID);
        return postCommentUseCase.execute(inputData);
    }

    /**
     * Executes the post comment use case with a specified comment ID.
     *
     * @param commentId the unique identifier for the comment.
     * @param commenter the username of the user posting the comment.
     * @param comment   the content/text of the comment.
     * @param movieID   the ID of the movie associated with the comment.
     * @return the output data returned by the use case.
     */
    public PostCommentOutputData execute(String commentId, String commenter, String comment, Integer movieID) {
        final PostCommentInputData inputData = new PostCommentInputData(commentId, commenter, comment, movieID);
        return postCommentUseCase.execute(inputData);
    }

    /**
     * Executes the use case to post a reply to an existing comment.
     *
     * @param commenter       the username of the user posting the reply.
     * @param reply           the content/text of the reply.
     * @param parentCommentID the ID of the comment being replied to.
     * @param movieID         the ID of the movie associated with the reply.
     * @return the output data returned by the use case.
     */
    public PostCommentOutputData executeReply(String commenter, String reply, String parentCommentID, Integer movieID) {
        final PostCommentInputData inputData = new PostCommentInputData(null,
                commenter, reply, movieID, parentCommentID);
        return postCommentUseCase.execute(inputData);
    }
}

