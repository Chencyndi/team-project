package movieapp.interface_adapter.comment;

import movieapp.interface_adapter.ViewModel;

/**
 * The View Model for the Post Comment View.
 */
public class PostCommentViewModel extends ViewModel<CommentState> {

    public static final String TITLE_LABEL = "Post Comment";
    public static final String COMMENTER_LABEL = "Username:";
    public static final String COMMENT_LABEL = "Comment:";
    public static final String POST_BUTTON_LABEL = "Post Comment";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    public PostCommentViewModel() {
        super("post comment");
    }
}

