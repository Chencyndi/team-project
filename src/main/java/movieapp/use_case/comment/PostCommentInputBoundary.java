package movieapp.use_case.comment;

public interface PostCommentInputBoundary {
    PostCommentOutputData execute(PostCommentInputData inputData);
}
