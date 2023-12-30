package dat3.dto;

import dat3.model.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {

    private int id;
    private String title;
    private String content;
    private String visibility;
    private String createdAt;
    private String updatedAt;
    private String username;
    private int likes;

    public PostDTO(int id, String title, String content, String visibility, String createdAt, String updatedAt, String username, int likes) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.visibility = visibility;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username = username;
        this.likes = likes;
    }

    public static PostDTO convertToDto(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getVisibility().toString(),
                post.getCreatedAt().toString(),
                post.getUpdatedAt() != null ? post.getUpdatedAt().toString() : null,
                post.getUser().getUsername(),
                post.getLikes()
        );
    }

    public static List<PostDTO> convertToDto(List<Post> posts) {
        return posts.stream().map(PostDTO::convertToDto).toList();
    }
}