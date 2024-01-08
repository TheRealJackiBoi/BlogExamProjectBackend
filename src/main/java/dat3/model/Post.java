package dat3.model;
import dat3.dto.PostDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "visibility", nullable = false)
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "likes")
    private int likes;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<User> haveLikedUsers = new HashSet<>();

    @ManyToOne
    private User user;

    public Post(String title, String content, Visibility visibility) {
        this.title = title;
        this.content = content;
        this.visibility = visibility;
    }

    public Post(String title, String content, Visibility visibility, User user) {
        this.title = title;
        this.content = content;
        this.visibility = visibility;
        this.user = user;
    }

    public Post(PostDTO postDto) {
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
        this.visibility = Visibility.valueOf(postDto.getVisibility());
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void setUser(User user) {
        this.user = user;
        user.addPost(this);
    }

    public void removeUser() {
        user.removePost(this);
        this.user = null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }



    public boolean like(User user) {
        if (haveLikedUsers.contains(user)) {
            System.out.println("This user have already liked this post");
            // false for failed like
            return false;
        }
        haveLikedUsers.add(user);
        likes += 1;
        // true for successful like
        return true;
    }

    public boolean unLike(User user) {
      if (!haveLikedUsers.contains(user)) {
          System.out.println("This user haven't liked this post");
          // false for failed unlike
          return false;
      }
      haveLikedUsers.remove(user);
      likes -= 1;
      // true for successful unlike
      return true;
    }
}