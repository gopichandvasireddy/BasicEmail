package email.model;

import email.enums.MailCategory;

import javax.persistence.*;

@Entity
public class MailMap {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer mailId;

    private Integer userId;

    private Integer senderId;

    @ManyToOne
    @JoinColumn(name = "mailId", nullable = false, insertable = false, updatable = false)
    private Mail mail;

//    @ManyToOne
//    @JoinColumn(name = "userId", nullable = false, insertable = false, updatable = false)
//    private User user;

    @Column(nullable=false, columnDefinition="boolean default false")
    private boolean isRead;

    @Enumerated(EnumType.STRING)
    private MailCategory category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMailId() {
        return mailId;
    }

    public void setMailId(Integer mailId) {
        this.mailId = mailId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public MailCategory getCategory() {
        return category;
    }

    public void setCategory(MailCategory category) {
        this.category = category;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }
}
