package com.looppan.looppan.controller.user;

import com.looppan.looppan.controller.user.utils.Captcha;
import com.looppan.looppan.controller.user.utils.StaticKey;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class GetPicCheckCode {

    @RequestMapping(value = "/getPicCheckCode", method = RequestMethod.GET)
    public void sendCheckCode(HttpServletResponse response, HttpSession session, @RequestParam Integer type) throws IOException {
        response.setContentType("image/png");

        String captcha = Captcha.generateRandomCaptcha(StaticKey.CHECK_CODE_LENGTH);
        BufferedImage image = Captcha.createCaptchaImage(captcha);

        session.setAttribute(StaticKey.PIC_CHECK_CODE_KEY, captcha);

        ImageIO.write(image, "png", response.getOutputStream());
    }
}
