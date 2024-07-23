package appmanager;

import cucumber.api.CucumberOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import tests.TestRunner;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;
@PropertySource("classpath:*.properties")
public class EmailSender {

    public JavaMailSender emailSender = emailSender();
    FileSystemResource file = new FileSystemResource(new File("./src/test/resources/testing_image.png"));
    private static PropertyFileReader localreader  = new PropertyFileReader("local.properties");
    @Value("${sender.email}")
    String email;
    @Value("${application.name}")
    String appname;


    public JavaMailSender emailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("email.fhlbny.com"); // 10.2.222.25    email.fhlbny.com
        mailSender.setPort(25);
        mailSender.setUsername(email);
        mailSender.setPassword("");
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.starttls.required", "false");
        props.put("mail.debug", "true");
        return mailSender;
    }


    public void sendHTMLmessage(String[] to, String subject, String text){

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper  helper = new MimeMessageHelper(message, true);
            helper.setFrom(localreader.get("sender.email"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text,true);
//                helper.addAttachment("Invoice", file);
            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String buildMessage(){

        String[] tags = TestRunner.class.getAnnotation(CucumberOptions.class).tags();
        StringBuilder executedScenarios = new StringBuilder();
        for(String name: tags){
            if(name.contains("@Smoke")){
                executedScenarios.append(" \n Smoke ,");
            } if(name.contains("@Regression")){
                executedScenarios.append("\n Regression ,");
            } if(name.contains("@Functional")){
                executedScenarios.append("\n Functional.");
            } else{
                executedScenarios.append("\n "+ name);
            }
        }
        StringBuilder tagName = new StringBuilder();
        for(String scenarios:tags){
            tagName.append(scenarios+" <br>");
        }
        BodyPart messageBodyPart = new MimeBodyPart();
        String  header = "<h3   style = 'font-weight:normal;font-family:calibri,garamond,serif;'>Hi Team,</h3>";
        String  body ="<div style = 'font-family:calibri,garamond,serif;font-size:16px;'>"+
                " <p>"+"Started to execute "+appname +" automated test cases. </p>"+
                " <p>"+"Test scenarios with the following tags will be executed: "+"</p>"+
                " <p>"+executedScenarios.toString()+ "</p>"+"</div>";
        String warningMessage = " <p><b>"+"THIS IS AN AUTOMATED MESSAGE - PLEASE DO NOT REPLY DIRECTLY TO THIS EMAIL!!!"+"</b></p>";
//                 "<img src='cid:resources/testing_image.png' height='100' width='800'/>";
        String footer = "<h3 style = 'font-weight:normal;font-family:calibri,garamond,serif;>Happy Testing! </h3>" +
                "<h3 style = 'font-weight:normal;font-family:calibri,garamond,serif;> SQE team </h3>";
        return warningMessage+header+body+footer;
    }

    public String buildPostMessage(ArrayList<String> scrioList){

        int failTotal = 0;
        int passTotal = 0;
        StringBuilder scenarioName = new StringBuilder();
        String  header = "<h3   style = 'font-weight:normal;font-family:calibri,garamond,serif;'>Hi Team,</h3>";
        for(String scenarios:scrioList){
            scenarioName.append("<li>"+scenarios+"</li>");
            if(scenarios.contains("FAIL")){
                failTotal++;
            }else{
                passTotal++;
            }
        }

        String warningMessage=  "<div style = 'font-family:calibri,garamond,serif;font-size:16px;' >" +
                " <h3>"+"THIS IS AN AUTOMATED MESSAGE - PLEASE DO NOT REPLY TO THIS EMAIL!!!"+"</h3> </div>";

        String  body ="<div style = 'font-family:calibri,garamond,serif;font-size:16px;'>"+
                " <p>"+" Execution of the "+localreader.get("application.name")+" status check automation test cases has completed. " +
                "<a href='http://jenkins:8080/job/development/job/sqe/job/Status_Check/job/SK_Repair_Queue/'> Click here to view the Jenkins Allure report</a></p>"+
                "<p><b> TOTAL PASSED TEST SCENARIOS - "+  passTotal+"</b></p>"+
                "<p><b> TOTAL FAILED TEST SCENARIOS - "+  failTotal+ " </b></p>"+
                "<p> The following tests were executed:</p>"+ scenarioName.toString()+"</div>";

        String footer = "<div style = 'font-family:calibri,garamond,serif;font-size:16px;' >" +
                " <p>If you have any questions or concerns please contact the SQE Team: SQE@fhlbny.com <br /><br />" +
                "Best regards, </br>" +
                "SQE team </p> </div>";



        return warningMessage+header+body+footer;///
    }


    public String buildPostTextMessage(ArrayList<String> scrioList) {

        int failTotal = 0;
        int passTotal = 0;
        StringBuilder scenarioName = new StringBuilder();
        String header = "<h3   style = 'font-weight:normal;font-family:calibri,garamond,serif;'>Hi Team,</h3>";
        for (String scenarios : scrioList) {
            scenarioName.append("<li>" + scenarios + "</li>");
            if (scenarios.contains("FAIL")) {
                failTotal++;
            } else {
                passTotal++;
            }
        }

        String body = "<div style = 'font-family:calibri,garamond,serif;font-size:16px;'>" +
                " <p>" + "Execution of the " + appname + " status check has been completed.PASSED-" + passTotal + ", FAILED-" + failTotal + " Check email for more details</p>";
        return header + body;
    }

    public String getHostName(){

        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            return addr.getHostName();
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
            return null;
        }
    }



}
