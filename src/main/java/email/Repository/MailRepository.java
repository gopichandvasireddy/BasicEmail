package email.Repository;

import email.model.Mail;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface MailRepository extends CrudRepository<Mail, Integer> {

    void deleteById(Integer id);
}
