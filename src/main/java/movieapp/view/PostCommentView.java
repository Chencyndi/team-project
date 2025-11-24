// PostCommentView.java
package movieapp.view;

import movieapp.interface_adapter.comment.PostCommentController;
import movieapp.interface_adapter.comment.PostCommentViewModel;
import movieapp.interface_adapter.login.LoginController;
import movieapp.use_case.comment.PostCommentOutputData;

import javax.swing.*;
import java.util.function.Supplier;


public class PostCommentView extends BasePostCommentView {
    private final Integer movieID;

    public PostCommentView(JFrame parent,
                           PostCommentController postCommentController,
                           PostCommentViewModel viewModel,
                           Integer movieID,
                           Supplier<String> currentUsernameSupplier,
                           Runnable onCommentPostedCallback,
                           LoginController loginController) {
        super(parent, postCommentController, viewModel, movieID, currentUsernameSupplier,
                onCommentPostedCallback, loginController);
        this.movieID = movieID;
        initializeView(500, 400, 10, 30, true);
        setTitle(getTitle());
    }

    public PostCommentView(PostCommentController postCommentController,
                           PostCommentViewModel viewModel,
                           Integer movieID) {
        this(null, postCommentController, viewModel, movieID, () -> null, null, null);
    }

    @Override
    public String getTitle() {
        return PostCommentViewModel.TITLE_LABEL;
    }

    @Override
    protected String getTextLabel() {
        return PostCommentViewModel.COMMENT_LABEL;
    }

    @Override
    protected String getPostButtonLabel() {
        return PostCommentViewModel.POST_BUTTON_LABEL;
    }

    @Override
    protected String getEmptyErrorMessage() {
        return "Comment cannot be empty";
    }

    @Override
    protected String getLoginRequiredMessage() {
        return "Please log in to post a comment.";
    }

    @Override
    protected String getSuccessMessage() {
        return "Comment posted successfully!";
    }

    @Override
    protected String getErrorDialogTitle() {
        return "Unable to Post Comment";
    }

    @Override
    protected String getErrorDialogMessage() {
        return "Unable to post comment.";
    }

    @Override
    protected PostCommentOutputData executePost(String username, String text) {
        return postCommentController.execute(username, text, movieID);
    }

    @Override
    protected PostCommentOutputData executeRetry(String username, String text) {
        return postCommentController.execute(username, text, movieID);
    }
}

