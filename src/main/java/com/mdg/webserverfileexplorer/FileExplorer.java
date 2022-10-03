package com.mdg.webserverfileexplorer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
public class FileExplorer {
    @GetMapping("/")
    public String test() {
        return "redirect:/fileExplorer";
    }

    @GetMapping("/fileExplorer")
    public String fileExplorerGet() {
        return "frame";
    }

    @GetMapping("/fileExplorer/left")
    public String leftGet(HttpServletRequest request, Model model) {
        String realPath = request.getServletContext().getRealPath("/");
        model.addAttribute("realPath", realPath);

        return "left";
    }

    @GetMapping("/api/getRoot")
    @ResponseBody
    public Object[] getRoot(HttpServletRequest request, @RequestParam(required = false) String id) {
        return getJSTreeData(request, id);
    }

    @GetMapping("/api/getChildren")
    @ResponseBody
    public Object[] getChildren(HttpServletRequest request, @RequestParam(required = false) String id) {
        return getJSTreeData(request, id);
    }

    private Object[] getJSTreeData(HttpServletRequest request, String id) {
        String realPath = id;
        if ("#".equals(id) || id == null) {
            realPath = request.getServletContext().getRealPath("/");
        }

        File file = new File(realPath);
        String[] fileNameArray = Objects.requireNonNull(file.list());
        File[] fileArray = Objects.requireNonNull(file.listFiles());
        Object[] array = new Object[fileNameArray.length];
        int count = 0;
        for (File childrenFile : fileArray) {
            Map<String, Object> map = new HashMap<>();

            map.put("id", childrenFile.getAbsolutePath());
            map.put("text", childrenFile.getName());
            map.put("children", true);
            if (childrenFile.isFile() || Objects.requireNonNull(childrenFile.listFiles()).length == 0) {
                map.put("children", false);
            }

            array[count] = map;
            count++;
        }

        return array;
    }

    @GetMapping("/fileExplorer/read")
    public String readGet(Model model, @RequestParam(required = false) String filePath) throws IOException {
        model.addAttribute("mode", "read");
        if (filePath == null || filePath.equals("")) {
            model.addAttribute("message", "데이터가 없거나 경로가 입력되지 않았습니다.");

            return "right";
        }
        File file = new File(filePath);
        if (!file.exists()) {
            model.addAttribute("message", "존재하지 않는 파일입니다.");
            model.addAttribute("filePath", filePath);

            return "right";
        }
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
    public String addGet(String filePath, Model model) {
        model.addAttribute("mode", "add");
        File file = new File(filePath);
        if (!file.isDirectory()) {
            model.addAttribute("message", "디렉터리가 아닙니다.");
            model.addAttribute("filePath", filePath);
            return "right";
        }
        model.addAttribute("message", "새로운 파일 작성");
        model.addAttribute("filePath", filePath);
        model.addAttribute("data", "");
        return "right";
    }

    @PostMapping("/fileExplorer/add")
    public String addPost(String filePath, String fileName, String fileContent, RedirectAttributes redirectAttributes) throws IOException {
        if (fileName == null) {
            fileName = "";
        }
        File file = new File(filePath + fileName);
        if (file.exists() && file.isDirectory()) {
            redirectAttributes.addFlashAttribute("message", "디렉터리가 이미 존재합니다.");
            return "redirect:/fileExplorer/read";
        }
        if (file.exists()) {
            redirectAttributes.addFlashAttribute("message", "파일이 이미 존재합니다.");
            return "redirect:/fileExplorer/read";
        }
        if (!file.createNewFile()) {
            redirectAttributes.addFlashAttribute("message", "파일을 생성하지 못했습니다.");
            return "redirect:/fileExplorer/read";
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.flush();
        fileWriter.close();
        return "redirect:/fileExplorer/read";
    }

    @PostMapping("/fileExplorer/write")
    public String writePost(String filePath, String fileName, String fileContent, RedirectAttributes redirectAttributes) throws IOException {
        File file = new File(filePath);
        if (isFileOrDirectoryPresent(file, redirectAttributes)) {
            return "redirect:/fileExplorer/read";
        }
        if (file.isDirectory() && fileName != null) {
            file = new File(filePath + fileName);
            if (!file.createNewFile()) {
                redirectAttributes.addFlashAttribute("message", "파일을 생성하지 못했습니다.");
                return "redirect:/fileExplorer/read";
            }
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
    public String deletePost(String filePath, RedirectAttributes redirectAttributes) {
        File file = new File(filePath);
        if (isFileOrDirectoryPresent(file, redirectAttributes)) {
            return "redirect:/fileExplorer/read";
        }
        if (file.delete()) {
            redirectAttributes.addFlashAttribute("message", "파일이 삭제되었습니다.");
            return "redirect:/fileExplorer/read";
        }
        redirectAttributes.addFlashAttribute("message", "파일 삭제를 실패하였습니다.");
        return "redirect:/fileExplorer/read";
    }

    @GetMapping("/fileExplorer/rename")
    public String renameGet(Model model, String filePath, String newName) {
        model.addAttribute("mode", "rename");
        model.addAttribute("message", "변경할 이름을 입력해주세요.");
        model.addAttribute("filePath", filePath);
        model.addAttribute("data", "");
        return "right";
    }

    @PostMapping("/fileExplorer/rename")
    public String renamePost(String filePath, String newName, RedirectAttributes redirectAttributes) {
        File currentFile = new File(filePath);
        if (isFileOrDirectoryPresent(currentFile, redirectAttributes)) {
            return "redirect:/fileExplorer/read";
        }
        File fileWithChangedName = new File(currentFile.getAbsolutePath().replace(currentFile.getName(), newName));
        if (currentFile.renameTo(fileWithChangedName)) {
            redirectAttributes.addFlashAttribute("message", "파일 이름이 변경되었습니다.");
            return "redirect:/fileExplorer/read";
        }
        redirectAttributes.addFlashAttribute("message", "파일 이름 변경를 실패하였습니다.");
        return "redirect:/fileExplorer/read";
    }

    public boolean isFileOrDirectoryPresent(File file, RedirectAttributes redirectAttributes) {
        if (!file.exists() && file.isDirectory()) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 디렉터리입니다.");
            return true;
        }
        if (!file.exists()) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 파일입니다.");
            return true;
        }
        return false;
    }
}
