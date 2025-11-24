package movieapp.use_case.comment;

public class PostCommentInputData {
    private final String comment;
    private final String commentID;
    private final String commenter;
    private final Integer movieID;
    private final String parentCommentID;

    public PostCommentInputData(String commentID, String commenter, String comment, Integer movieID) {
        this(commentID, commenter, comment, movieID, null);
    }

    public PostCommentInputData(String commentID, String commenter, String comment,
                                Integer movieID, String parentCommentID) {
        this.comment = comment;
        this.commentID = commentID;
        this.commenter = commenter;
        this.movieID = movieID;
        this.parentCommentID = parentCommentID;
    }

    public String getComment() {
        return comment;
    }
    public String getCommentID() {
        return commentID;
    }
    public String getCommenter() {
        return commenter;
    }
    public Integer getMovieID() {
        return movieID;
    }
    public String getParentCommentID() {
        return parentCommentID;
    }
}
