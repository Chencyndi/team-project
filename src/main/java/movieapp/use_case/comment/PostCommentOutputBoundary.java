package movieapp.use_case.comment;

public interface PostCommentOutputBoundary {
    /**
     * Prepares the view for a validation error (e.g., empty comment).
     *
     * @param errorMessage the explanation of why validation failed.
     * @return the output data with the error state set.
     */
    PostCommentOutputData presentValidationError(String errorMessage);

    /**
     * Prepares the view when the user is not found or not logged in.
     *
     * @param errorMessage the explanation of the error.
     * @return the output data with the error state set.
     */
    PostCommentOutputData presentUserNotFound(String errorMessage);

    /**
     * Prepares the view for a successful comment submission.
     *
     * @param outputData the output data containing the successfully created comment.
     * @return the formatted output data for the view.
     */
    PostCommentOutputData presentSuccess(PostCommentOutputData outputData);
}
