package email.controller;

import email.enums.MailCategory;
import email.model.MailMap;
import email.resource.SendEmailResource;
import email.resource.UpdateEmailResource;
import email.resource.UserResource;
import email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class EmailController {

    @Autowired
    EmailService emailService;

    /**
     * Get mails for a particular category {Ex. INBOX, SENT}.
     * @param email
     * @param mailCategory
     * @return
     */
    @RequestMapping(path = "/mail",method = RequestMethod.GET)
    public List<MailMap> getMail(@RequestParam String email, @RequestParam(value = "mail_category") MailCategory mailCategory){
        return emailService.getMail(email, mailCategory);
    }

    /**
     * Send mails to a user.
     * @param emailResource
     * @return
     */
    @PostMapping(path = "/mail")
    public ResponseEntity<HttpStatus> sendMail(@Valid @RequestBody SendEmailResource emailResource){
        if(emailService.sendMail(emailResource)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    /**
     * Save mail as Draft.
     * @param emailResource
     * @return
     */
    @PostMapping(path = "/draft")
    public ResponseEntity<HttpStatus> draft(@Valid @RequestBody SendEmailResource emailResource){
        if(emailService.makeDraft(emailResource)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    /**
     * Send Draft.
     * @param emailResource
     * @return
     */
    @PostMapping(path = "/senddraft")
    public ResponseEntity<HttpStatus> sendDraft(@Valid @RequestBody SendEmailResource emailResource){
        if(emailService.sendDraft(emailResource)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    /**
     * Mark Email as Read.
     * @param emailResource
     * @return
     */
    @PostMapping(path = "/read")
    public ResponseEntity<HttpStatus> read(@Valid @RequestBody UpdateEmailResource emailResource){
        if(emailService.markRead(emailResource)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    /**
     * Create a User.
     * @param userResource
     * @return
     */
    @PostMapping(path = "/create")
    public ResponseEntity<HttpStatus> create(@Valid @RequestBody UserResource userResource){
        if(emailService.createUser(userResource)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    /**
     * Delete mails to trash.
     * @return
     */
    @PostMapping(path = "/trash")
    public ResponseEntity<HttpStatus> trashMail(@Valid @RequestBody UpdateEmailResource emailResource){
        if(emailService.moveToTrash(emailResource)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    /**
     * Permanently delete mails from trash.
     * @return
     */
    @PostMapping(path = "/deletetrash")
    public ResponseEntity<HttpStatus> deleteTrash(@Valid @RequestBody UpdateEmailResource emailResource){
        if(emailService.deleteTrash(emailResource)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }

    /**
     * Permanently delete mails.
     * @return
     */
    @PostMapping(path = "/delete")
    public ResponseEntity<HttpStatus> deletePermanently(@Valid @RequestBody UpdateEmailResource emailResource){
        if(emailService.deletePermanently(emailResource)) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.valueOf(400));
    }
}
