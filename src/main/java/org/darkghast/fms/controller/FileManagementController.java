package org.darkghast.fms.controller;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.darkghast.fms.entity.SavedFile;
import org.darkghast.fms.entity.User;
import org.darkghast.fms.service.FileDeleteService;
import org.darkghast.fms.service.SavedFileService;
import org.darkghast.fms.service.impl.FileDeleteServiceImpl;
import org.darkghast.fms.utils.PageQueryUtil;
import org.darkghast.fms.utils.SavedFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 包含对已保存的文件进行操作的方法
 */
@Controller
@RequestMapping(value = "FileManagement", name = "保存文件相关Controller")
public class FileManagementController {
    /**
     * 用于记录日志信息
     */
    static final Logger log = Logger.getLogger(FileManagementController.class.getName());
    /**
     * 用于调用SavedFileService
     */
    private SavedFileService savedFileService;

    /**
     * 查询所有数据库中保存的文件信息
     *
     * @return 以JSON将查询到的数据返回给前端
     */
    @RequestMapping(value = "queryAll",name = "获取文件信息")
    @ResponseBody
    public List<SavedFile> queryAll(){
        return savedFileService.queryAll();
    }

    /**
     * 分页查询所有文件信息并返回给前端
     */
    @RequestMapping(value = "queryByPage",name = "分页查询获取文件信息")
    @ResponseBody
    public Map<String,Object> queryByPage(HttpServletRequest request) {
        Map<String,Object> result = new HashMap<>();
        int pageNo = Integer.parseInt(request.getParameter("pageNo").trim());
        result.put("count",(savedFileService.getCount()+ PageQueryUtil.PAGE_SIZE-1)/PageQueryUtil.PAGE_SIZE);
        result.put("savedFileList",savedFileService.queryAllByPage(pageNo, PageQueryUtil.PAGE_SIZE));
        return result;
    }

    /**
     * 按条件分页查询用户信息并返回给前端
     */
    @RequestMapping(value = "queryByPageForCondition",name = "按条件分页查询获取文件信息")
    @ResponseBody
    public Map<String,Object> queryByPageForCondition(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        SavedFile savedFile=new SavedFile();
        User user=new User();
        Long id=null;
        Integer uploaderId=null;
        String realFilename="",username="";
        int pageNo = Integer.parseInt(request.getParameter("pageNo").trim());
        if(!request.getParameter("id").equals("")&&request.getParameter("id")!=null){
            id = Long.parseLong(request.getParameter("id").trim());
        }
        if(!request.getParameter("uploadUserId").equals("")&&request.getParameter("uploadUserId")!=null){
            uploaderId = Integer.parseInt(request.getParameter("uploadUserId").trim());
        }
        if(!request.getParameter("filename").equals("")&&request.getParameter("filename")!=null){
            realFilename = request.getParameter("filename").trim();
        }
        if(!request.getParameter("uploadUsername").equals("")&&request.getParameter("uploadUsername")!=null){
            username = request.getParameter("uploadUsername").trim();
        }

        user.setId(uploaderId);
        user.setUsername("%"+username+"%");

        savedFile.setRealFileName("%"+realFilename+"%");
        savedFile.setId(id);
        savedFile.setUploader(user);

        result.put
                ("count",(savedFileService.getCountForCondition(savedFile)+PageQueryUtil.PAGE_SIZE-1)/PageQueryUtil.PAGE_SIZE);
        result.put("savedFileList",savedFileService.queryByPageForCondition(savedFile,pageNo, PageQueryUtil.PAGE_SIZE));
        return result;
    }

    /**
     * 用前端传来的id进行数据库以及保存的文件的删除
     */
    @RequestMapping(value = "delete",name = "删除文件信息")
    @ResponseBody
    public int delete(HttpServletRequest request) throws IOException {
        FileDeleteService fileDeleteService = new FileDeleteServiceImpl();
        long id = Long.parseLong(request.getParameter("id").trim());
        SavedFile savedFile = savedFileService.query(id);
        List<SavedFile> savedFileList = savedFileService.queryAllByIdentifier(savedFile.getIdentifier());
        File deleteFile = new File(savedFile.getCompletePath());
        int result = fileDeleteService.delete(id);

        /*
          删除文件的条件
          1.返回结果result为1，即数据库中已删除文件信息
          2.数据库中不存在相同的文件
         */
        if (result == 1
                && savedFileList.size() == 1
                && savedFileList.get(0).getId() == id) {
            if (deleteFile.delete()) {
                //成功删除后再删除数据库数据
                fileDeleteService.commit();
                User user = (User) request.getSession().getAttribute("user");
                log.info("用户ID-" + user.getId() + "-删除了文件" + savedFile.getSavedFileName());
            } else {
                fileDeleteService.rollback();
                result = 0;
            }
        } else {
            //如果无需删除文件，则直接删除数据库中的文件数据
            fileDeleteService.commit();
        }
        return result;
    }

    /**
     * 接收前端传来的id及修改的文件名的字段，将数据库中的文件名进行修改
     */
    @RequestMapping(value = "update",name = "更新文件信息")
    @ResponseBody
    public int update(HttpServletRequest request) throws IOException {
        SavedFile savedFile = new SavedFile();
        savedFile.setId(Long.parseLong(request.getParameter("id").trim()));
        savedFile.setRealFileName(request.getParameter("realname").trim());
        return savedFileService.update(savedFile);
    }

    /**
     * 下载文件
     */
    @RequestMapping(value = "downLoad",name = "下载文件")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = Long.parseLong(request.getParameter("id").trim());

        //从服务器获取文件的详细信息
        SavedFile savedFile = savedFileService.query(id);
        // 下载的文件的完整路径
        String completePath = savedFile.getCompletePath();//下载路径
        //传输给前端的文件的文件名
        String filename = savedFile.getRealFileName();
        //转换编码避免出现乱码
        String filenameToISO= new String(filename.getBytes(StandardCharsets.UTF_8),"ISO8859-1");

        FileInputStream in; // 输入流
        ServletOutputStream out; // 输出流
        try {
            // 设置下载文件使用的报头
            response.setHeader("Content-Type", "application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename="
                    + filenameToISO);
            // 读入文件
            in = new FileInputStream(completePath);
            // 得到响应对象的输出流，用于向客户端输出二进制数据
            out = response.getOutputStream();
            out.flush();
            int aRead;
            byte[] b = new byte[1024];
            while ((aRead = in.read(b)) != -1) {
                out.write(b, 0, aRead);
            }
            out.flush();
            in.close();
            out.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存前端上传的文件，并将文件信息存入数据库
     */
    @RequestMapping(value = "saveFile",name = "上传文件")
    @ResponseBody
    public Map<String,Object> saveFile(@RequestParam("uploadFiles") List<MultipartFile> files, HttpServletRequest request)
            throws IOException {
        //用于返回上传失败的文件信息
        Map<String,Object> result = new HashMap<>();
        List<SavedFile> errorList = new ArrayList<>();

        //获取上传者信息
        User uploader = new User();
        HttpSession session = request.getSession();
        uploader.setId(Integer.parseInt(session.getAttribute("userId").toString()));

        //文件保存的路径
        String realpath = SavedFileUtils.getRootPath();
        File path = new File(realpath);
        //如果目录不存在则先创建
        if (!path.exists()) {
            path.mkdirs();
        }


        for (MultipartFile file : files) {
            if(file.isEmpty()&&files.get(0)==file
                    &&file.getOriginalFilename()!=null&&file.getOriginalFilename().equals("")
                    &&file.getContentType()!=null&&file.getContentType().equals("application/octet-stream")){
                //全都满足说明是“头文件”，直接跳过
                continue;
            }

            //先得到将要保存的文件的信息
            SavedFile savedFile = new SavedFile();
            savedFile.setId(null);
            savedFile.setRealFileName(file.getOriginalFilename());
            savedFile.setUploader(uploader);
            savedFile.setUploadTime(SavedFileUtils.getTime());
            savedFile.setPath(SavedFileUtils.getRandomSavePath(Objects.requireNonNull(file.getOriginalFilename())));
            //如果源文件文件名过长，则去除源文件文件名（不作为后缀）
            if (file.getOriginalFilename().length() > 155) {
                savedFile.setSavedFileName(SavedFileUtils.getPrefix(session));
            } else {
                savedFile.setSavedFileName(SavedFileUtils.getPrefix(session) + file.getOriginalFilename());
            }
            savedFile.setIdentifier(SavedFileUtils.getIdentifier(file.getBytes()));
            try {
                List<SavedFile> savedFileList = savedFileService.queryAllByIdentifier(savedFile.getIdentifier());
                //如果数据库不存在相同（Identifier识别码）的文件才在介质中保存
                if (savedFileService.queryAllByIdentifier(savedFile.getIdentifier()).size() == 0) {
                    FileUtils.copyInputStreamToFile(file.getInputStream(),
                            new File(savedFile.getSavePath(), savedFile.getSavedFileName()));
                } else {
                    //存在的话将文件名改为数据库中相同文件的文件名
                    savedFile.setSavedFileName(savedFileList.get(0).getSavedFileName());
                }

                //文件保存后将信息保存到数据库
                savedFileService.save(savedFile);
            } catch (IOException e) {
                //如果上传错误则将上传的文件删除
                new File(savedFile.getSavePath(), savedFile.getSavedFileName()).delete();
                //将上传错误的文件名添加到errorList，在上传任务完成后返回《上传错误的文件名》的JSON数据
                errorList.add(savedFile);
            }
        }
        result.put("errorList",errorList);
        return result;
    }

    @Autowired
    public void setSavedFileService(SavedFileService savedFileService) {
        this.savedFileService = savedFileService;
    }
}