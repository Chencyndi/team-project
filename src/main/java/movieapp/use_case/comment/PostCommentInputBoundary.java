package movieapp.use_case.comment;

public interface PostCommentInputBoundary {
    /**
     * Executes the post comment use case logic.
     *
     * @param inputData the input data containing the necessary information to post a comment.
     * @return the output data containing the result of the operation.
     */
    PostCommentOutputData execute(PostCommentInputData inputData);
}
