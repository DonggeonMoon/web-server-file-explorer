package com.mdg.webserverfileexplorer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
public class FileExplorer {
    @GetMapping("/fileExplorer")
    public String fileExplorerGet() {
        return "frame";
    }

    @GetMapping("/fileExplorer/left")
    public String leftGet(HttpServletRequest request, Model model) {
        model.addAttribute("realPath", request.getServletContext().getRealPath("/"));

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
        String[] fileNames = Objects.requireNonNull(file.list());
        File[] files = Objects.requireNonNull(file.listFiles());
        Object[] array = new Object[fileNames.length];
        int count = 0;
        for (File childrenFile : files) {
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
    public String readGet(Model model, @RequestParam(required = false) String path) throws IOException {
        model.addAttribute("mode", "read");
        model.addAttribute("path", path);
        if (path == null || path.equals("")) {
            model.addAttribute("message", "???????????? ????????? ????????? ???????????? ???????????????.");

            return "right";
        }
        File file = new File(path);
        if (!file.exists()) {
            model.addAttribute("message", "???????????? ?????? ???????????????.");

            return "right";
        }
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder(1000);
        int n;
        while ((n = bufferedReader.read()) != -1) {
            stringBuilder.append((char) n);
        }
        fileReader.close();
        model.addAttribute("fileName", file.getName());
        model.addAttribute("data", stringBuilder.toString());

        return "right";
    }

    @PostMapping("/fileExplorer/write")
    public String writePost(String path, String fileContent, RedirectAttributes redirectAttributes) throws IOException {
        File file = new File(path);
        if (file.isDirectory() && !file.mkdirs()) {
            redirectAttributes.addFlashAttribute("message", "????????? ???????????? ???????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        if (file.isDirectory() && file.exists()) {
            redirectAttributes.addFlashAttribute("message", "??????????????? ??????????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(fileContent);
        bufferedWriter.close();
        fileWriter.close();

        return "redirect:/fileExplorer/read";
    }

    @GetMapping("/fileExplorer/addFile")
    public String addFileGet(String path, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("mode", "addFile");
        model.addAttribute("path", path);
        File file = new File(path);
        if (file.isDirectory()) {
            redirectAttributes.addFlashAttribute("path", path);
            redirectAttributes.addFlashAttribute("message", "?????? ?????? ??????????????? ???????????? ????????? ??????????????????.");
            return "redirect:/fileExplorer/read?path=" + path;
        }
        model.addAttribute("message", "??? ?????? ??????");
        model.addAttribute("data", "??? ??????");

        return "right";
    }

    @PostMapping("/fileExplorer/addFile")
    public String addFilePost(String path, String fileName, String fileContent, Model model, RedirectAttributes redirectAttributes) throws IOException {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            redirectAttributes.addFlashAttribute("message", "??????????????? ?????? ???????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        if (file.exists()) {
            redirectAttributes.addFlashAttribute("message", "????????? ?????? ???????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        if (file.isDirectory() && !file.mkdirs()) {
            redirectAttributes.addFlashAttribute("message", "??????????????? ???????????? ???????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        if (file.isDirectory() && file.exists()) {
            redirectAttributes.addFlashAttribute("message", "??????????????? ??????????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        if (!file.createNewFile()) {
            redirectAttributes.addFlashAttribute("message", "????????? ???????????? ???????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(fileContent);
        bufferedWriter.close();
        fileWriter.close();
        model.addAttribute("message", "????????? ??????????????????.");

        return "redirect:/fileExplorer/read";
    }

    @GetMapping("/fileExplorer/addDirectory")
    public String addDirectoryGet(String path, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("mode", "addDirectory");
        model.addAttribute("path", path);
        File file = new File(path);
        if (file.isFile()) {
            redirectAttributes.addFlashAttribute("path", path);
            redirectAttributes.addFlashAttribute("message", "???????????? ?????? ??????????????? ?????? ????????? ??????????????????.");
            return "redirect:/fileExplorer/read?path=" + path;
        }
        model.addAttribute("message", "??? ???????????? ??????");
        model.addAttribute("data", "??? ????????????");

        return "right";
    }

    @PostMapping("/fileExplorer/addDirectory")
    public String addDirectoryPost(String path, String fileName, String fileContent, Model model, RedirectAttributes redirectAttributes) throws IOException {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            redirectAttributes.addFlashAttribute("message", "??????????????? ?????? ???????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        if (file.exists()) {
            redirectAttributes.addFlashAttribute("message", "????????? ?????? ???????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        if (file.isDirectory() && !file.mkdirs()) {
            redirectAttributes.addFlashAttribute("message", "??????????????? ???????????? ???????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        if (file.isDirectory() && file.exists()) {
            redirectAttributes.addFlashAttribute("message", "??????????????? ??????????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        if (!file.createNewFile()) {
            redirectAttributes.addFlashAttribute("message", "????????? ???????????? ???????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(fileContent);
        bufferedWriter.close();
        fileWriter.close();
        model.addAttribute("message", "????????? ??????????????????.");

        return "redirect:/fileExplorer/read?path=" + path;
    }

    @GetMapping("/fileExplorer/rename")
    public String renameGet(Model model, String path, RedirectAttributes redirectAttributes) {
        model.addAttribute("mode", "renameFile");
        model.addAttribute("path", path);
        model.addAttribute("message", "????????? ????????? ??????????????????.");
        File file = new File(path);
        if (isFileOrDirectoryPresent(file, redirectAttributes)) {
            return "redirect:/fileExplorer/read?path=" + path;
        }
        if (file.isDirectory()) {
            model.addAttribute("mode", "renameDirectory");

            return "right";
        }
        model.addAttribute("data", "");

        return "right";
    }

    @PostMapping("/fileExplorer/rename")
    public String renamePost(String path, String fileName, String newName, RedirectAttributes redirectAttributes) {
        File currentFile = new File(new File(path).getPath() + fileName);
        if (isFileOrDirectoryPresent(currentFile, redirectAttributes)) {
            redirectAttributes.addFlashAttribute("message", "????????? ?????? ???????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        File fileWithChangedName = new File(currentFile.getAbsolutePath().replace(currentFile.getName(), newName));
        if (currentFile.renameTo(fileWithChangedName)) {
            redirectAttributes.addFlashAttribute("message", "?????? ????????? ?????????????????????.");

            return "redirect:/fileExplorer/read?path=" + path;
        }
        redirectAttributes.addFlashAttribute("message", "?????? ?????? ????????? ?????????????????????.");

        return "redirect:/fileExplorer/read?path=" + path;
    }

    private boolean isFileOrDirectoryPresent(File file, RedirectAttributes redirectAttributes) {
        if (file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            redirectAttributes.addFlashAttribute("message", "???????????? ?????? ?????????????????????.");

            return false;
        }
        redirectAttributes.addFlashAttribute("message", "???????????? ?????? ???????????????.");

        return false;
    }
}
