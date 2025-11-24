package movieapp.entity;

import java.util.ArrayList;
import java.util.List;

public class Comment {
    private final String commentID;
    private final User commenter;
    private List<Comment> replies = new ArrayList<>();
    private final String text;
    private final Integer movieID;

    public Comment(String commentID, User commenter, List<Comment> replies, String text, Integer movieID) {
        this.commentID = commentID;
        this.commenter = commenter;
        this.replies = replies;
        this.text = text;
        this.movieID = movieID;
    }

    public String getCommentID() {
        return commentID;
    }

    public User getCommenter() {
        return commenter;
    }

    public List<Comment> getReplies() {return replies;}

    public void addReply(Comment reply) {replies.add(reply);}

    public String getText() {
        return text;
    }

    public Integer getMovieID() {return movieID;}
}
