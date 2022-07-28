package com.mdg.webserverfileexplorer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Controller
public class FileExplorer {
    @GetMapping("/fileExplorer")
    public String fileExplorer(HttpServletRequest request, Model model) throws IOException {
        return "frame";
    }

    @GetMapping("/fileExplorer/left")
    public String left(HttpServletRequest request, Model model) throws IOException {
        String realPath = request.getServletContext().getRealPath("/");
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        File file = new File(realPath);

        File[] fileArray = file.listFiles();

        model.addAttribute("realPath", realPath);
        model.addAttribute("contextPath", contextPath);
        model.addAttribute("servletPath", servletPath);
        model.addAttribute("file", file);
        model.addAttribute("data", fileArray);
        return "left";
    }

    @GetMapping("/fileExplorer/right")
    public String right(HttpServletRequest request, Model model, @RequestParam(value = "filePath", required = false) String filePath) throws IOException {
        if (filePath == null ||  filePath == "") {
            return "right";
        }
        File file = new File(filePath);

        FileReader fileReader = new FileReader(file);
        StringBuilder stringBuilder = new StringBuilder(1000);
        int n = 0;
        while((n = fileReader.read()) != -1) {
            stringBuilder.append((char) n);
        }
        model.addAttribute("data", stringBuilder.toString());
        return "right";
    }
}
