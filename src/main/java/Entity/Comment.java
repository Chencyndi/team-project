package Entity;

public class Comment {
    private final String commentID;
    private final User commenter;
    private final Comment parentComment;
    private final String text;

    public Comment(String commentID, User commenter, Comment parentComment, String text) {
        this.commentID = commentID;
        this.commenter = commenter;
        this.parentComment = parentComment;
        this.text = text;
    }
    public  String getCommentID() {
        return commentID;
    }
    public User getCommenter() {
        return commenter;
    }
    public Comment getParentComment() {
        return parentComment;
    }
    public String getText() {
        return text;
    }
}
