package movieapp.use_case.comment;

public interface PostCommentOutputBoundary {
    PostCommentOutputData presentValidationError(String errorMessage);
    PostCommentOutputData presentUserNotFound(String errorMessage);
    PostCommentOutputData presentSuccess(PostCommentOutputData outputData);
}
