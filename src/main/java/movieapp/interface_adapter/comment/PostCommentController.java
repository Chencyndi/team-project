package movieapp.interface_adapter.comment;

import movieapp.use_case.comment.PostCommentInputBoundary;
import movieapp.use_case.comment.PostCommentInputData;
import movieapp.use_case.comment.PostCommentOutputData;

public class PostCommentController {
    private final PostCommentInputBoundary postCommentUseCase;

    public PostCommentController(PostCommentInputBoundary postCommentUseCase) {
        this.postCommentUseCase = postCommentUseCase;
    }

    public PostCommentOutputData execute(String commenter, String comment, Integer movieID) {
        PostCommentInputData inputData = new PostCommentInputData(null, commenter, comment, movieID);
        return postCommentUseCase.execute(inputData);
    }

    public PostCommentOutputData execute(String commentId, String commenter, String comment, Integer movieID) {
        PostCommentInputData inputData = new PostCommentInputData(commentId, commenter, comment, movieID);
        return postCommentUseCase.execute(inputData);
    }

    public PostCommentOutputData executeReply(String commenter, String reply, String parentCommentID, Integer movieID) {
        PostCommentInputData inputData = new PostCommentInputData
                (null, commenter, reply, movieID, parentCommentID);
        return postCommentUseCase.execute(inputData);
    }
}

