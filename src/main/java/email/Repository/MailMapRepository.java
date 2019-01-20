package email.Repository;

import email.enums.MailCategory;
import email.model.MailMap;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface MailMapRepository extends JpaRepository<MailMap, Integer> {

    List<MailMap> findByUserId(Integer userId);

    List<MailMap> findBySenderId(Integer senderId);

    List<MailMap> findBySenderIdAndCategory(Integer senderId, MailCategory category);

    List<MailMap> findByUserIdAndCategory(Integer userId, MailCategory category);

    MailMap findByUserIdAndMailId(Integer userId, Integer MailId);

    void deleteByUserIdAndMailId(Integer userId, Integer MailId);

    void deleteByMailId(Integer MailId);

    Long countByMailId(Integer MailId);
}
