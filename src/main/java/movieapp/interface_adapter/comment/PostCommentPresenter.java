package movieapp.interface_adapter.comment;

import movieapp.use_case.comment.PostCommentOutputBoundary;
import movieapp.use_case.comment.PostCommentOutputData;

/**
 * The presenter for the Post Comment Use Case.
 */
public class PostCommentPresenter implements PostCommentOutputBoundary {
    private final PostCommentViewModel viewModel;

    public PostCommentPresenter(PostCommentViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public PostCommentOutputData presentValidationError(String errorMessage) {
        CommentState state = viewModel.getState();
        if (state == null) {
            state = new CommentState();
        }
        state.setCommentError(errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChange();
        return new PostCommentOutputData(null, null, null);
    }

    @Override
    public PostCommentOutputData presentUserNotFound(String errorMessage) {
        CommentState state = viewModel.getState();
        if (state == null) {
            state = new CommentState();
        }
        state.setCommentError(errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChange();
        return new PostCommentOutputData(null, null, null);
    }

    @Override
    public PostCommentOutputData presentSuccess(PostCommentOutputData outputData) {
        CommentState state = viewModel.getState();
        if (state == null) {
            state = new CommentState();
        }
        state.setCommentError(null);
        state.setCommentId(outputData.getCommentId());
        viewModel.setState(state);
        viewModel.firePropertyChange();
        return outputData;
    }
}

