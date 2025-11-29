package movieapp.use_case.comment;

import movieapp.entity.Comment;
import movieapp.entity.User;
import movieapp.use_case.common.UserDataAccessInterface;

import java.util.ArrayList;
import java.util.UUID;

public class PostCommentInteractor implements PostCommentInputBoundary {
    private final CommentDataAccessInterface commentDataAccess;
    private final UserDataAccessInterface userDataAccess;
    private final PostCommentOutputBoundary presenter;

    public PostCommentInteractor(CommentDataAccessInterface commentDataAccess,
                                 UserDataAccessInterface userDataAccess,
                                 PostCommentOutputBoundary presenter) {
        this.commentDataAccess = commentDataAccess;
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public PostCommentOutputData execute(PostCommentInputData inputData) {
        if (inputData.getCommenter() == null || inputData.getCommenter().isBlank()) {
            return presenter.presentValidationError("username can't be null");
        }
        if (inputData.getComment() == null || inputData.getComment().isBlank()) {
            return presenter.presentValidationError("comments can't be null");
        }

        User commenter = userDataAccess.findByUsername(inputData.getCommenter());
        if (commenter == null) {
            return presenter.presentUserNotFound("user is not exist");
        }

        if (inputData.getMovieID() == null) {
            return presenter.presentValidationError("Movie ID is required for posting a comment");
        }

        String commentId = inputData.getCommentID() != null ? inputData.getCommentID() : UUID.randomUUID().toString();

        Comment comment = new Comment(commentId, commenter, new ArrayList<>(), inputData.getComment().trim(),
                inputData.getMovieID());

        if (inputData.getParentCommentID() != null) {
            //REPLY
            commentDataAccess.addReply(comment, inputData.getParentCommentID());
        } else {
            //COMMENT
            commentDataAccess.addComment(comment);
        }

        PostCommentOutputData outputData = new PostCommentOutputData(
                commentId,
                commenter.getUsername(),
                comment.getText()
        );
        return presenter.presentSuccess(outputData);
    }
}

