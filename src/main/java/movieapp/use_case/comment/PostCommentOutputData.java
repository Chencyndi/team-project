package movieapp.use_case.comment;

public class PostCommentOutputData {
    private final String commentId;
    private final String commenter;
    private final String text;

    public PostCommentOutputData(String commentId, String commenter, String text) {
        this.commentId = commentId;
        this.commenter = commenter;
        this.text = text;
    }
    public String getCommentId() {
        return commentId;
    }
    public String getCommenter() {
        return commenter;
    }
    public String getText() {
        return text;
    }
}
