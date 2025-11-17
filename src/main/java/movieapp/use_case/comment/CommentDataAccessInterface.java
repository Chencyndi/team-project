package movieapp.use_case.comment;

import movieapp.entity.Comment;

import java.util.List;

public interface CommentDataAccessInterface {
    List<Comment> getComments(Integer movieID);
    void addComment(Comment comment);
    void addReply(Comment comment);

}
