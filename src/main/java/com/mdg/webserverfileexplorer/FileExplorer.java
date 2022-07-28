package com.mdg.webserverfileexplorer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Controller
public class FileExplorer {
    @GetMapping("/fileExplorer")
    public String fileExplorerGet() {
        return "frame";
    }

    @GetMapping("/fileExplorer/left")
    public String leftGet(HttpServletRequest request, Model model) {
        String realPath = request.getServletContext().getRealPath("/");
        File file = new File(realPath);

        File[] fileArray = file.listFiles();

        model.addAttribute("realPath", realPath);
        model.addAttribute("file", file);
        model.addAttribute("data", fileArray);
        return "left";
    }

    @GetMapping("/fileExplorer/read")
    public String readGet(Model model, @RequestParam(value = "filePath", required = false) String filePath) throws IOException {
        if (filePath == null || filePath.equals("")) {
            model.addAttribute("message", "데이터가 없거나 경로가 입력되지 않았습니다.");
            return "right";
        }
        File file = new File(filePath);

        if (file.isDirectory()) {
            model.addAttribute("message", "디렉터리입니다.");
            return "right";
        }

        FileReader fileReader = new FileReader(file);
        StringBuilder stringBuilder = new StringBuilder(1000);
        int n;
        while ((n = fileReader.read()) != -1) {
            stringBuilder.append((char) n);
        }
        fileReader.close();
        model.addAttribute("filePath", filePath);
        model.addAttribute("data", stringBuilder.toString());
        return "right";
    }

    @GetMapping("/fileExplorer/add")
    public String addGet(String filePath, Model model) throws IOException {
        model.addAttribute("message", "새로운 파일 작성");
        model.addAttribute("filePath", filePath);
        return "right";
    }

    @PostMapping("/fileExplorer/write")
    public String writePost(String filePath, String fileName, String fileContent, RedirectAttributes redirectAttributes) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 파일입니다.");
            return "redirect:/fileExplorer/read";
        }
        if (file.isDirectory() && fileName != null) {
            file = new File(filePath + fileName);
            file.createNewFile();
        }
        if (file.isDirectory()) {
            redirectAttributes.addFlashAttribute("message", "디렉터리입니다.");
            return "redirect:/fileExplorer/read";
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.flush();
        fileWriter.close();
        return "redirect:/fileExplorer/read";
    }

    @PostMapping("/fileExplorer/delete")
    public String deletePost(String filePath, RedirectAttributes redirectAttributes) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 파일입니다.");
            return "redirect:/fileExplorer/read";
        }
        if (file.isDirectory()) {
            redirectAttributes.addFlashAttribute("message", "디렉터리입니다.");
            return "redirect:/fileExplorer/read";
        }
        file.delete();
        return "redirect:/fileExplorer/read";
    }

    @PostMapping("/fileExplorer/rename")
    public String renamePost(String filePath, String fileName, RedirectAttributes redirectAttributes) throws IOException {
        File currentFile = new File(filePath);
        if (!currentFile.exists()) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 파일입니다.");
            return "redirect:/fileExplorer/read";
        }
        File fileWithChangedName = new File(currentFile.getAbsolutePath().replace(currentFile.getName(), fileName));
        currentFile.renameTo(fileWithChangedName);
        return "redirect:/fileExplorer/read";
    }
}
