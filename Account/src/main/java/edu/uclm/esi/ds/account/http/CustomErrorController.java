package edu.uclm.esi.ds.account.http;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

	@RequestMapping("/error")
	@ResponseBody
	public String handleError(HttpServletRequest request) {
        int statusCode = (int) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");

        return String.format("<html><body><h2>Error :(</h2><div><b>Status code</b>: %s</div>"
                        + "<div><b>Message</b>: %s</div><body></html>",
                statusCode, (exception == null) ? "No message": exception.getMessage());
	}
}
