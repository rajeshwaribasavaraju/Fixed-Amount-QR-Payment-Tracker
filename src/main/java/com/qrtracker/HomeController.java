
package com.qrtracker;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/welcome")
    @ResponseBody
    public String home() {
        return """
                <html>
                <head>
                    <title>Fixed Amount QR Tracker</title>
                </head>
                <body>
                    <h1>Welcome to Fixed Amount QR Tracker!</h1>
                    <p>Your application is running successfully.</p>
                </body>
                </html>
                """;
    }
}
