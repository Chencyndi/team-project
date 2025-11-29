package movieapp.use_case.comment;

import java.util.ArrayList;
import java.util.UUID;

import movieapp.entity.Comment;
import movieapp.entity.User;
import movieapp.use_case.common.UserDataAccessInterface;

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
        final PostCommentOutputData outputData;
        if (inputData.getCommenter() == null || inputData.getCommenter().isBlank()) {
            outputData = presenter.presentValidationError("username can't be null");
        }
        else if (inputData.getComment() == null || inputData.getComment().isBlank()) {
            outputData = presenter.presentValidationError("comments can't be null");
        }
        else if (inputData.getMovieID() == null) {
            outputData = presenter.presentValidationError("Movie ID is required for posting a comment");
        }
        else {
            final User commenter = userDataAccess.findByUsername(inputData.getCommenter());

            if (commenter == null) {
                outputData = presenter.presentUserNotFound("user is not exist");
            }
            else {
                final String commentId;
                if (inputData.getCommentID() != null) {
                    commentId = inputData.getCommentID();
                }
                else {
                    commentId = UUID.randomUUID().toString();
                }
                final Comment comment = new Comment(commentId, commenter, new ArrayList<>(),
                        inputData.getComment().trim(), inputData.getMovieID());

                if (inputData.getParentCommentID() != null) {
                    commentDataAccess.addReply(comment, inputData.getParentCommentID());
                }
                else {
                    commentDataAccess.addComment(comment);
                }

                final PostCommentOutputData successData = new PostCommentOutputData(
                        commentId,
                        commenter.getUsername(),
                        comment.getText()
                );
                outputData = presenter.presentSuccess(successData);
            }
        }
        return outputData;
    }
}

