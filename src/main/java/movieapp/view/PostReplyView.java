// PostReplyView.java
package movieapp.view;

import movieapp.interface_adapter.comment.PostCommentController;
import movieapp.interface_adapter.comment.PostCommentViewModel;
import movieapp.interface_adapter.login.LoginController;
import movieapp.use_case.comment.PostCommentOutputData;

import javax.swing.*;
import java.util.function.Supplier;

/**
 * The view for replying to a comment.
 * This is a small dialog that appears directly under a comment.
 */
public class PostReplyView extends BasePostCommentView {
    private final String parentCommentID;
    private final Integer movieID;

    public PostReplyView(JFrame parent,
                         PostCommentController postCommentController,
                         PostCommentViewModel viewModel,
                         Integer movieID,
                         String parentCommentID,
                         Supplier<String> currentUsernameSupplier,
                         Runnable onReplyPostedCallback,
                         LoginController loginController) {
        super(parent, postCommentController, viewModel, movieID, currentUsernameSupplier,
                onReplyPostedCallback, loginController);
        this.parentCommentID = parentCommentID;
        this.movieID = movieID;
        initializeView(400, 200, 4, 25, false);
        setTitle(getTitle());
    }

    public PostReplyView(JFrame parent,
                         PostCommentController postCommentController,
                         PostCommentViewModel viewModel,
                         Integer movieID,
                         String parentCommentID) {
        this(parent, postCommentController, viewModel, movieID, parentCommentID, () -> null, null, null);
    }

    @Override
    public String getTitle() {
        return "Reply to Comment";
    }

    @Override
    protected String getTextLabel() {
        return "Reply:";
    }

    @Override
    protected String getPostButtonLabel() {
        return "Post Reply";
    }

    @Override
    protected String getEmptyErrorMessage() {
        return "Comment cannot be empty";
    }

    @Override
    protected String getLoginRequiredMessage() {
        return "Please log in to post a reply.";
    }

    @Override
    protected String getSuccessMessage() {
        return "Reply posted successfully!";
    }

    @Override
    protected String getErrorDialogTitle() {
        return "Unable to Post Comment";
    }

    @Override
    protected String getErrorDialogMessage() {
        return "Unable to post reply.";
    }

    @Override
    protected PostCommentOutputData executePost(String username, String text) {
        return postCommentController.executeReply(username, text, parentCommentID, movieID);
    }

    @Override
    protected PostCommentOutputData executeRetry(String username, String text) {
        return postCommentController.executeReply(username, text, parentCommentID, movieID);
    }
}

