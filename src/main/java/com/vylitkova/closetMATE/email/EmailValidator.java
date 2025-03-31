package com.vylitkova.closetMATE.email;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String> {

    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public boolean test(String email) {
        return email != null && pattern.matcher(email).matches();
    }

    public String buildEmail(String name, String link){
        String logoUrl = "http://localhost:8080/Logo.png";
        return " <section style=\"background: #fff; display: flex; flex-direction: column; align-items: center; justify-content: center; border-radius: 15px; text-align: center;\">\n" +
                "        <div style=\"width: 100%; height: 100%; background: #FFDCEF; padding: 3.5rem 0 2rem; border-radius: 15px 15px 0 0;\">\n" +
                "            <img src=\""+logoUrl+"\" alt=\"\" width=\"100rem\" style=\"margin: 0; \">\n" +
                "        </div>\n" +
                "       <div style=\"display: flex; flex-direction: column; align-items: center; justify-content: center; gap:2rem; padding: 0rem 2rem 2rem;  font-size: 1.5rem;  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\">\n" +
                "            <h2 style=\"margin: 1rem 0 0 0;\">Email Confirmation</h2>\n" +
                "            <p style=\"margin: 0; line-height: 2rem;\">Hi, <span style=\"font-style: italic;\">"+name+"!</span><br> Thank you for signing up to our fashion <br> planner service: <span style=\"font-weight: 500;\">ClosetMATE</span> !</p>\n" +
                "            <p style=\"margin: 0; line-height: 2rem;\">Please verify your email by clicking the link below. It will expire in 15 minutes.</p>\n" +
                "            <a href=\""+link+"\" style=\"margin: 0; font-family: inherit; text-decoration: none;font-weight: 500; color: #000; background: #FFDCEF; width:50%; padding: 0.4rem 0rem; border-radius: 20px;\">VERIFY EMAIL</a>\n" +
                "            <hr style=\"width: 100%; opacity: 0.8;\">\n" +
                "            <p style=\"color: rgba(0, 0, 0, 0.6); text-align: left; align-self: flex-start; margin: 0;\">Sincerely, <br>ClosetMATE team.</p>\n" +
                "       </div>\n" +
                "    </section>" ;
    }

    public String buildEmailPasswordChange(String name){
        String logoUrl = "http://localhost:8080/Logo.png";
        return " <section style=\"background: #fff; display: flex; flex-direction: column; align-items: center; justify-content: center; border-radius: 15px; text-align: center;\">\n" +
                "        <div style=\"width: 100%; height: 100%; background: #FFDCEF; padding: 3.5rem 0 2rem; border-radius: 15px 15px 0 0;\">\n" +
                "            <img src=\""+logoUrl+"\" alt=\"\" width=\"100rem\" style=\"margin: 0; \">\n" +
                "        </div>\n" +
                "       <div style=\"display: flex; flex-direction: column; align-items: center; justify-content: center; gap:2rem; padding: 0rem 2rem 2rem;  font-size: 1.5rem;  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\">\n" +
                "            <h2 style=\"margin: 1rem 0 0 0;\">Password change</h2>\n" +
                "            <p style=\"margin: 0; line-height: 2rem;\">Hi, <span style=\"font-style: italic;\">"+name+"!</span><br> Your password was successfully changed! <br> Thanks for using <span style=\"font-weight: 500;\">ClosetMATE</span> !</p>\n" +
                "            <hr style=\"width: 100%; opacity: 0.8;\">\n" +
                "            <p style=\"color: rgba(0, 0, 0, 0.6); text-align: left; align-self: flex-start; margin: 0;\">Sincerely, <br>ClosetMATE team.</p>\n" +
                "       </div>\n" +
                "    </section>" ;
    }
}
