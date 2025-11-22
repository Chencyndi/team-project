package movieapp.data_access;

import movieapp.entity.Comment;
import movieapp.entity.User;
import movieapp.use_case.comment.CommentDataAccessInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentDataAccessObject implements CommentDataAccessInterface {
    private final Path FILE_PATH = Paths.get("src/main/java/movieapp/data/comments.json");
    private final UserDataAccessObject userDataAccess = new UserDataAccessObject();

    private JSONArray readJSONFile() {
        try {
            String jsonString = Files.readString(FILE_PATH);
            return new JSONArray(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeJSONFile(JSONArray jsonArray) {
        try {
            Files.writeString(FILE_PATH, jsonArray.toString(4));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Comment> getComments(Integer movieID) {
        JSONArray jsonArray = readJSONFile();
        Map<String, Comment> map = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            if (obj.getInt("movieID") == movieID) {
                String id = obj.get("commentID").toString();
                String username = obj.getString("username");
                User user = userDataAccess.findByUsername(username);

                if (user != null) {
                    map.put(id, new Comment(id, user, new ArrayList<>(), obj.getString("text"), movieID));
                }
            }
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            if (obj.getInt("movieID") != movieID) continue;

            Comment parent = map.get(obj.getString("commentID"));
            if (parent == null) continue;

            JSONArray replyIDs = obj.getJSONArray("replies");

            for (int r = 0; r < replyIDs.length(); r++) {
                Comment child = map.get(replyIDs.getString(r));
                if (child != null) {
                    parent.addReply(child);
                }
            }
        }

        List<String> allReplyIDs = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.getInt("movieID") == movieID) {
                JSONArray replyIDs = obj.getJSONArray("replies");
                for (int r = 0; r < replyIDs.length(); r++) {
                    allReplyIDs.add(replyIDs.getString(r));
                }
            }
        }

        List<Comment> topLevel = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.getInt("movieID") == movieID) {
                String commentID = obj.getString("commentID");
                if (!allReplyIDs.contains(commentID)) {
                    Comment comment = map.get(commentID);
                    if (comment != null) {
                        topLevel.add(comment);
                    }
                }
            }
        }

        return topLevel;
    }

    @Override
    public void addComment(Comment comment) {
        JSONArray jsonArray = readJSONFile();

        JSONObject newObj = new JSONObject();
        newObj.put("commentID", comment.getCommentID());
        newObj.put("username", comment.getCommenter().getUsername());
        newObj.put("movieID", comment.getMovieID());
        newObj.put("text", comment.getText());
        newObj.put("replies", new JSONArray());

        jsonArray.put(newObj);
        writeJSONFile(jsonArray);

    }
    public void addReply(Comment reply, String parentCommentID) {
        JSONArray jsonArray = readJSONFile();
        JSONObject replyObj = new JSONObject();
        replyObj.put("commentID", reply.getCommentID());
        replyObj.put("username", reply.getCommenter().getUsername());
        replyObj.put("movieID", reply.getMovieID());
        replyObj.put("text", reply.getText());
        replyObj.put("replies", new JSONArray());
        jsonArray.put(replyObj);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.getString("commentID").equals(parentCommentID)) {
                JSONArray replies = obj.getJSONArray("replies");
                replies.put(reply.getCommentID());
                break;
            }
        }

        writeJSONFile(jsonArray);

    }
}
