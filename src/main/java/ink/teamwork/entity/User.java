package ink.teamwork.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Table(name = "users")
public class User implements Serializable {

    public static String TYPE_USER = "USER";
    public static String TYPE_ADMIN = "ADMIN";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String mobile;

    private String password;

    private Date createdTime;

    private String idCard;

    private String name;

    @Column(unique = true)
    private String username;

    private Long channelId;

    private int status;

    private String type;

    private String description;

}
