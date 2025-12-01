package data_access;
import movieapp.entity.Comment;
import movieapp.entity.User;
import movieapp.use_case.comment.CommentDataAccessInterface;
import movieapp.use_case.common.UserDataAccessInterface;

import java.util.*;

public class InMemoryCommentDAO implements CommentDataAccessInterface {

    private final UserDataAccessInterface userDataAccess;
    private final Map<String, CommentData> comments = new HashMap<>();

    private static class CommentData {
        String commentID;
        String username;
        Integer movieID;
        String text;
        List<String> replyIDs;

        CommentData(String commentID, String username, Integer movieID, String text) {
            this.commentID = commentID;
            this.username = username;
            this.movieID = movieID;
            this.text = text;
            this.replyIDs = new ArrayList<>();
        }
    }

    public InMemoryCommentDAO(UserDataAccessInterface userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    @Override
    public List<Comment> getComments(Integer movieID) {
        Map<String, Comment> commentMap = new HashMap<>();

        for (CommentData data : comments.values()) {
            if (data.movieID.equals(movieID)) {
                User user = userDataAccess.findByUsername(data.username);
                if (user != null) {
                    Comment comment = new Comment(data.commentID, user, new ArrayList<>(),data.text,data.movieID);
                    commentMap.put(data.commentID, comment);
                }
            }
        }

        for (CommentData data : comments.values()) {
            if (data.movieID.equals(movieID)) {
                Comment parent = commentMap.get(data.commentID);
                if (parent != null) {
                    for (String replyID : data.replyIDs) {
                        Comment child = commentMap.get(replyID);
                        if (child != null) {
                            parent.addReply(child);
                        }
                    }
                }
            }
        }

        Set<String> allReplyIDs = new HashSet<>();
        for (CommentData data : comments.values()) {
            if (data.movieID.equals(movieID)) {
                allReplyIDs.addAll(data.replyIDs);
            }
        }

        List<Comment> topLevel = new ArrayList<>();
        for (CommentData data : comments.values()) {
            if (data.movieID.equals(movieID) && !allReplyIDs.contains(data.commentID)) {
                Comment comment = commentMap.get(data.commentID);
                if (comment != null) {
                    topLevel.add(comment);
                }
            }
        }

        return topLevel;
    }

    @Override
    public void addComment(Comment comment) {
        CommentData data = new CommentData(comment.getCommentID(), comment.getCommenter().getUsername(), comment.getMovieID(), comment.getText());

        comments.put(comment.getCommentID(), data);
    }

    @Override
    public void addReply(Comment reply, String parentCommentID) {
        CommentData replyData = new CommentData(reply.getCommentID(), reply.getCommenter().getUsername(), reply.getMovieID(), reply.getText());

        comments.put(reply.getCommentID(), replyData);

        CommentData parent = comments.get(parentCommentID);
        if (parent != null) {
            parent.replyIDs.add(reply.getCommentID());
        } else {
            throw new IllegalArgumentException("Parent comment not found: " + parentCommentID);
        }
    }

    public void clear() {
        comments.clear();
    }

    public int getCommentCount() {
        return comments.size();
    }
}