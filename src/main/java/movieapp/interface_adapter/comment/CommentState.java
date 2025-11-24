package movieapp.interface_adapter.comment;

public class CommentState {
    private String commenter = "";
    private String comment = "";
    private String commentError;
    private String commentId;

    public String getCommenter() {
        return commenter;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentError() {
        return commentError;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCommentError(String commentError) {
        this.commentError = commentError;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}

