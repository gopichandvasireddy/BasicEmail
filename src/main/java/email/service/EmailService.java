package email.service;

import email.Repository.MailMapRepository;
import email.Repository.MailRepository;
import email.Repository.UserRepository;
import email.enums.MailCategory;
import email.model.Mail;
import email.model.MailMap;
import email.model.User;
import email.resource.SendEmailResource;
import email.resource.UpdateEmailResource;
import email.resource.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private MailMapRepository mailMapRepository;

    /**
     * get Mails.
     * @param email
     * @param mailCategory
     * @return
     */
    public List<MailMap> getMail(String email, MailCategory mailCategory){
        Integer userId = getUserId(email);
        if(mailCategory.equals(MailCategory.SENT)) return getSentMails(userId);
        if(mailCategory.equals(MailCategory.DRAFT)) return getDrafts(userId);
        List<MailMap> mailMapList = mailMapRepository.findByUserIdAndCategory(userId, mailCategory);
        return mailMapList;
    }

    /**
     * get sent Mails.
     * @param senderId
     * @return
     */
    public List<MailMap> getSentMails(Integer senderId){
        List<MailMap> mailMapList = mailMapRepository.findBySenderIdAndCategory(senderId, MailCategory.INBOX);
        return mailMapList;
    }

    /**
     * get Drafts.
     * @param senderId
     * @return
     */
    public List<MailMap> getDrafts(Integer senderId){
        List<MailMap> mailMapList = mailMapRepository.findBySenderIdAndCategory(senderId, MailCategory.DRAFT);
        return mailMapList;
    }

    private Integer getUserId(String email){
        User user = userRepository.findByEmail(email);
        if(user==null) return null;
        return user.getId();
    }

    /**
     * Send Mails.
     * @param emailResource
     * @return
     */
    public boolean sendMail(SendEmailResource emailResource){
        Integer senderId = getUserId(emailResource.getSender());

        List<Integer> userIdList = getRecipients(emailResource.getRecipient());
        if(userIdList.size()==0) return false;

        Integer mailId = saveMail(emailResource);

        List<MailMap> mailMapList = getMailMapList(userIdList, mailId, senderId);
        mailMapRepository.saveAll(mailMapList);
        return true;
    }

    /**
     * Send Draft to recipients.
     * @param emailResource
     * @return
     */
    public boolean sendDraft(SendEmailResource emailResource){
        Integer senderId = getUserId(emailResource.getSender());

        Optional<Mail> optionalMail = mailRepository.findById(emailResource.getMailId());
        if(!optionalMail.isPresent()) return false;
        Mail mail = optionalMail.get();
        if(!mail.getAuthor().equals(emailResource.getSender())) return false;
        mail.setBody(emailResource.getBody());
        mail.setSubject(emailResource.getSubject());

        List<Integer> userIdList = getRecipients(emailResource.getRecipient());
        if(userIdList.size()==0) return false;

        mailRepository.save(mail);
        mailMapRepository.deleteByMailId(mail.getId());

        List<MailMap> mailMapList = getMailMapList(userIdList, mail.getId(), senderId);
        mailMapRepository.saveAll(mailMapList);
        return true;
    }

    /**
     * get recipient_id list for list of email_ids.
     * @param recipients
     * @return
     */
    private List<Integer> getRecipients(List<String> recipients){
        List<Integer> userIdList = new ArrayList<>();
        Iterator<String> iterator = recipients.iterator();
        Integer userId;
        while(iterator.hasNext()){
            userId = getUserId(iterator.next());
            if(userId!=null) userIdList.add(userId);
        }
        return userIdList;
    }


    private List<MailMap> getMailMapList(List<Integer> userIdList, Integer mailId, Integer senderId){
        Iterator<Integer> idIterator = userIdList.iterator();
        List<MailMap> mailMapList = new ArrayList<>();
        while(idIterator.hasNext()) {
            MailMap mailMap = new MailMap();
            mailMap.setMailId(mailId);
            mailMap.setUserId(idIterator.next());
            mailMap.setSenderId(senderId);
            mailMap.setCategory(MailCategory.INBOX);
            mailMapList.add(mailMap);
        }
        return mailMapList;
    }

    /**
     * Create a new user.
     * @param userResource
     * @return
     */
    public boolean createUser(UserResource userResource){
        User temp = userRepository.findByEmail(userResource.getEmail());
        if(temp!=null) return false;
        User user = new User();
        user.setEmail(userResource.getEmail());
        user.setName(userResource.getName());
        userRepository.save(user);
        return true;
    }


    /**
     * Mark mail as read.
     * @param emailResource
     * @return
     */
    public boolean markRead(UpdateEmailResource emailResource){
        Integer userId = getUserId(emailResource.getEmail());

        MailMap mailMap = mailMapRepository.findByUserIdAndMailId(userId, emailResource.getMailId());
        if(mailMap==null) return false;
        mailMap.setRead(true);
        mailMapRepository.save(mailMap);
        return true;
    }

    /**
     * Save as Draft.
     * @param emailResource
     * @return
     */
    public boolean makeDraft(SendEmailResource emailResource){
        Integer senderId = getUserId(emailResource.getSender());
        if(senderId==null) return false;

        Integer mailId = saveMail(emailResource);

        MailMap mailMap = new MailMap();
        mailMap.setMailId(mailId);
        mailMap.setSenderId(senderId);
        mailMap.setCategory(MailCategory.DRAFT);
        mailMapRepository.save(mailMap);
        return true;
    }

    /**
     * Move mail to trash.
     * @param emailResource
     * @return
     */
    public boolean moveToTrash(UpdateEmailResource emailResource){
        Integer userId = getUserId(emailResource.getEmail());

        MailMap mailMap = mailMapRepository.findByUserIdAndMailId(userId, emailResource.getMailId());
        if(mailMap==null) return false;
        mailMap.setCategory(MailCategory.TRASH);
        mailMapRepository.save(mailMap);
        return true;
    }

    /**
     * delete trash.
     * @param emailResource
     * @return
     */
    public boolean deleteTrash(UpdateEmailResource emailResource){
        Integer userId = getUserId(emailResource.getEmail());
        mailMapRepository.deleteByUserIdAndMailId(userId, emailResource.getMailId());

        Long count = mailMapRepository.countByMailId(emailResource.getMailId());
        if(count==0) mailRepository.deleteById(emailResource.getMailId());
        return true;
    }

    /**
     * delete a Mail permanently.
     * @param emailResource
     * @return
     */
    public boolean deletePermanently(UpdateEmailResource emailResource){
        Integer userId = getUserId(emailResource.getEmail());
        mailMapRepository.deleteByUserIdAndMailId(userId, emailResource.getMailId());

        Long count = mailMapRepository.countByMailId(emailResource.getMailId());
        if(count==0) mailRepository.deleteById(emailResource.getMailId());
        return true;
    }

    /**
     * Save Mail and return mail_id.
     * @param emailResource
     * @return
     */
    private Integer saveMail(SendEmailResource emailResource){
        Mail mail = new Mail();
        mail.setAuthor(emailResource.getSender());
        mail.setBody(emailResource.getBody());
        mail.setSubject(emailResource.getSubject());
        mail = mailRepository.save(mail);
        return mail.getId();
    }

}
