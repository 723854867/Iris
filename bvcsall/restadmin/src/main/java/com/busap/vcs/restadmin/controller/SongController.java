package com.busap.vcs.restadmin.controller;

import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.model.SingerDisplay;
import com.busap.vcs.service.Constants;
import com.busap.vcs.data.model.SongDisplay;
import com.busap.vcs.restadmin.utils.ResultData;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.SongService;
import com.busap.vcs.service.utils.ZipUtils;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.Page;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 音乐管理
 * Created by busap on 2016/5/30.
 */
@Controller
@RequestMapping("/song")
public class SongController extends CRUDController<Song, Long> {

    @Value("${files.localpath}")
    private String basePath;

    @Resource
    private SongService songService;

    @Override
    public void setBaseService(BaseService<Song, Long> baseService) {
        this.baseService = baseService;
    }

    //添加歌手分类
    @RequestMapping("insertSingerType")
    @ResponseBody
    public ResultData insertSingerType(@ModelAttribute("singerType") @Valid SingerType singerType, BindingResult results){
        ResultData resultData = new ResultData();
        singerType.setCreatorId(U.getUid());
        singerType.setCreateDate(new Date());
        singerType.setState(Constants.NORMAL);
        int result = songService.insertSingerType(singerType);
        if(result > 0){
            resultData.setResultCode("success");
            resultData.setResultMessage("添加成功！");
        }else{
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败！");
        }
        return resultData;
    }

    //添加歌手
    @RequestMapping("insertSinger")
    @ResponseBody
    public ResultData insertSinger(@ModelAttribute("singer") @Valid Singer singer, BindingResult results){
        ResultData resultData = new ResultData();
        singer.setCreatorId(U.getUid());
        singer.setState(Constants.NORMAL);
        singer.setCreateDate(new Date());
        int result = songService.insertSinger(singer);
        if(result > 0){
            resultData.setResultCode("success");
            resultData.setResultMessage("添加成功！");
        }else{
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败！");
        }
        return resultData;
    }

    //添加专辑
    @RequestMapping("insertAlbum")
    @ResponseBody
    public ResultData insertAlbum(@RequestParam(value = "albumCoverUrl", required = false) MultipartFile albumCoverUrl,@ModelAttribute("album") @Valid Album album, BindingResult results){
        ResultData resultData = new ResultData();
        album.setCreatorId(U.getUid());
        album.setCreateDate(new Date());
        album.setState(Constants.NORMAL);
        String albumCover = uploadFile(albumCoverUrl, "song", "albumCover");
        String[] fileArray = albumCover.split(",");
        if(fileArray.length > 0) {
            album.setAlbumCover(fileArray[0]);
        }
        int result = songService.insertAlbum(album);
        if(result > 0){
            resultData.setResultCode("success");
            resultData.setResultMessage("添加成功！");
        }else{
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败！");
        }
        return resultData;
    }

    //添加歌曲
    @RequestMapping("insertSong")
    @ResponseBody
    public ResultData insertSong(MultipartFile lyricFile,MultipartFile songFile,@ModelAttribute("song") @Valid Song song, BindingResult results) throws IOException {
        ResultData resultData = new ResultData();
        if (lyricFile.isEmpty()) {
            resultData.setResultCode("fail");
            resultData.setResultMessage("请至少选择一个mp3或m4a格式的文件");
            return resultData;
        }

        song.setCreatorId(U.getUid());
        song.setCreateDate(new Date());
        song.setState(Constants.NORMAL);
        String filePrefix = DateFormatUtils.format(new Date(), "HHmmss");
        String filePath = File.separator+"song"+File.separator+"packageFile"+File.separator+DateFormatUtils.format(new Date(), "yyyyMMdd")+File.separator;
        File mFile = new File(basePath + filePath, filePrefix+".lrc");
        FileUtils.copyInputStreamToFile(lyricFile.getInputStream(), mFile);
        File sFile = null;
        if(song.getType() == 1) {
            sFile = new File(basePath + filePath, filePrefix + ".mp3");
        }else{
            sFile = new File(basePath + filePath, filePrefix + ".m4a");
        }
        FileUtils.copyInputStreamToFile(songFile.getInputStream(), sFile);
        List<File> fileList = new ArrayList<File>();
        fileList.add(mFile);
        fileList.add(sFile);
        String fileName = filePrefix+".zip";
        ZipUtils.createZip(fileName,basePath + filePath,fileList,filePrefix);
        song.setPackageUrl(filePath+fileName);
        File file = new File(basePath+filePath+fileName);
        song.setPackageSize(file.length());
        song.setDownloadCount(0L);
        String singerId = song.getSingerId();
        if(singerId.substring(0,1).equals(",")){
            singerId = singerId.substring(1,singerId.length());
            song.setSingerId(singerId);
        }
        //TODO 需进行事务处理
        int result = songService.insertSong(song);


        if(singerId.indexOf(",")>=0){
            String[] singerArray = singerId.split(",");
            for(int i=0;i<singerArray.length;i++){
                SongSinger ss = new SongSinger();
                ss.setSingerId(Long.valueOf(singerArray[i]));
                ss.setSongId(song.getId());
                ss.setCreateDate(new Date());
                int ret = songService.insertSongSinger(ss);
            }
        }else{
            SongSinger ss = new SongSinger();
            ss.setSongId(song.getId());
            ss.setCreateDate(new Date());
            ss.setSingerId(Long.valueOf(singerId));
            int ret = songService.insertSongSinger(ss);
        }

        if(result > 0){
            resultData.setResultCode("success");
            resultData.setResultMessage("添加成功！");
        }else{
            resultData.setResultCode("fail");
            resultData.setResultMessage("添加失败！");
        }
        return resultData;
    }


    @RequestMapping("forwardSongList")
    public ModelAndView forwardSongList() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("selected","song");
        mav.setViewName("music/query_song");
        return mav;
    }

    @RequestMapping("forwardSingerTypeList")
    public ModelAndView forwardSingerTypeList() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("selected","singerType");
        mav.setViewName("music/query_singer_type");
        return mav;
    }

    @RequestMapping("forwardSingerList")
    public ModelAndView forwardSingerList() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("selected","singer");
        mav.setViewName("music/query_singer");
        return mav;
    }

    @RequestMapping("forwardAlbumList")
    public ModelAndView forwardAlbumList() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("selected","album");
        mav.setViewName("music/query_album");
        return mav;
    }

    @RequestMapping("querySongList")
    @ResponseBody
    @EnablePaging
    public Map<String, Object> querySongList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                             @RequestParam(value = "id", required = false) Long id,
                                             @RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "singerType", required = false) Integer singerType,
                                             @RequestParam(value = "singerName", required = false) String singerName,
                                             @RequestParam(value = "albumId", required = false) Long albumId,
                                             @RequestParam(value = "type", required = false) Integer type,
                                             @RequestParam(value = "state", required = false) Integer state) {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("id",id);
        params.put("name",name);
        params.put("singerType",singerType);
        params.put("singerName",singerName);
        params.put("albumId",albumId);
        params.put("type",type);
        params.put("state",state);
        List<SongDisplay> list = songService.querySongAll(params);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    @RequestMapping("querySingerTypeList")
    @ResponseBody
    @EnablePaging
    public Map<String, Object> querySingerTypeList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                             @RequestParam(value = "id", required = false) Long id,
                                             @RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "state", required = false) Integer state) {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("id",id);
        params.put("name",name);
        params.put("state",state);
        List<SingerType> list = songService.querySingerTypeAll(params);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    @RequestMapping("querySingerList")
    @ResponseBody
    @EnablePaging
    public Map<String, Object> querySingerList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                             @RequestParam(value = "id", required = false) Long id,
                                             @RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "singerType", required = false) Integer singerType,
                                             @RequestParam(value = "state", required = false) Integer state) {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("id",id);
        params.put("name",name);
        params.put("singerType",singerType);
        params.put("state",state);
        List<SingerDisplay> list = songService.querySingerAll(params);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    @RequestMapping("queryAlbumList")
    @ResponseBody
    @EnablePaging
    public Map<String, Object> queryAlbumList(@ModelAttribute("queryPage") JQueryPage queryPage,
                                               @RequestParam(value = "id", required = false) Long id,
                                               @RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "state", required = false) Integer state) {
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("id",id);
        params.put("name",name);
        params.put("state",state);
        List<Album> list = songService.queryAlbumAll(params);
        Page page = PagingContextHolder.getPage();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("total", page.getTotalResult());
        resultMap.put("rows", list);
        return resultMap;
    }

    @RequestMapping("updateSongTemplate")
    public ModelAndView updateSongTemplate(Long id) {
        ModelAndView mav = new ModelAndView();
        Song song = songService.selectByPrimaryKey(id);
        mav.addObject("song", song);
        mav.setViewName("music/update_song");
        return mav;
    }

    @RequestMapping("updateSingerTemplate")
    public ModelAndView updateSingerTemplate(Long id) {
        ModelAndView mav = new ModelAndView();
        Singer singer = songService.querySingerByPrimaryKey(id);
        mav.addObject("singer", singer);
        mav.setViewName("music/update_singer");
        return mav;
    }

    //更新歌手信息
    @RequestMapping("updateSinger")
    @ResponseBody
    public ResultData updateSinger(@ModelAttribute("singer") @Valid Singer singer, BindingResult results) {
        ResultData resultData = new ResultData();
        Singer singerObject = songService.querySingerByPrimaryKey(singer.getId());
        if (singerObject != null) {
            int ret = songService.updateSingerByPrimaryKey(singer);
            if (ret > 0) {
                resultData.setResultCode("success");
                resultData.setResultMessage("更新成功");
            } else {
                resultData.setResultCode("fail");
                resultData.setResultMessage("更新失败");
            }
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("数据不存在");
        }
        return resultData;
    }

    @RequestMapping("updateSingerTypeTemplate")
    public ModelAndView updateSingerTypeTemplate(Long id) {
        ModelAndView mav = new ModelAndView();
        SingerType singerType = songService.querySingerTypeByPrimaryKey(id);
        mav.addObject("singerType", singerType);
        mav.setViewName("music/update_singer_type");
        return mav;
    }

    //更新歌手类型信息
    @RequestMapping("updateSingerType")
    @ResponseBody
    public ResultData updateSingerType(@ModelAttribute("singerType") @Valid SingerType singerType, BindingResult results) {
        ResultData resultData = new ResultData();
        SingerType singerTypeObject = songService.querySingerTypeByPrimaryKey(singerType.getId());
        if (singerTypeObject != null) {
            int ret = songService.updateSingerTypeByPrimaryKey(singerType);
            if (ret > 0) {
                resultData.setResultCode("success");
                resultData.setResultMessage("更新成功");
            } else {
                resultData.setResultCode("fail");
                resultData.setResultMessage("更新失败");
            }
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("数据不存在");
        }
        return resultData;
    }

    @RequestMapping("updateAlbumTemplate")
    public ModelAndView updateAlbumTemplate(Long id) {
        ModelAndView mav = new ModelAndView();
        Album album = songService.queryAlbumByPrimaryKey(id);
        mav.addObject("album", album);
        mav.setViewName("music/update_album");
        return mav;
    }

    @RequestMapping("updateAlbum")
    @ResponseBody
    public ResultData updateAlbum(@RequestParam(value = "albumCover", required = false) MultipartFile albumCover,
                                 @ModelAttribute("album") @Valid Album album, BindingResult results) {
        ResultData resultData = new ResultData();
        Album albumObject = songService.queryAlbumByPrimaryKey(album.getId());
        if (albumObject != null) {
            if (!albumCover.isEmpty()) {
                String format = albumCover.getOriginalFilename().substring(albumCover.getOriginalFilename().lastIndexOf("."));
                if (!(com.busap.vcs.base.Constants.IMAGE_FORMAT.indexOf(format.toLowerCase()) != -1)) {
                    resultData.setResultCode("fail");
                    resultData.setResultMessage("请选择jpg，jpeg，png，gif，icon格式的文件");
                    return resultData;
                }
                String fileInfo = uploadFile(albumCover, "song", "albumCover");
                String[] fileArray = fileInfo.split(",");
                album.setAlbumCover(fileArray[0]);
            }
            int ret = songService.updateAlbumByPrimaryKey(album);
            if (ret > 0) {
                resultData.setResultCode("success");
                resultData.setResultMessage("更新成功");
            } else {
                resultData.setResultCode("fail");
                resultData.setResultMessage("更新失败");
            }
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("数据不存在");
        }
        return resultData;
    }

    //更新歌曲信息
    @RequestMapping("updateSong")
    @ResponseBody
    public ResultData updateSong(@RequestParam(value = "lyricFile", required = false) MultipartFile lyricFile,
                                 @RequestParam(value = "songFile", required = false) MultipartFile songFile,
                                 @ModelAttribute("song") @Valid Song song, BindingResult results) throws IOException {
        ResultData resultData = new ResultData();
        Song songObject = songService.selectByPrimaryKey(song.getId());
        if (songObject != null) {
            if (!songFile.isEmpty() && !lyricFile.isEmpty()) {
                String filePrefix = DateFormatUtils.format(new Date(), "HHmmss");
                String filePath = File.separator+"song"+File.separator+"packageFile"+File.separator+DateFormatUtils.format(new Date(), "yyyyMMdd")+File.separator;
                File mFile = new File(basePath + filePath, filePrefix+".lrc");
                FileUtils.copyInputStreamToFile(lyricFile.getInputStream(), mFile);
                File sFile = null;
                if(song.getType() == 1) {
                    sFile = new File(basePath + filePath, filePrefix + ".mp3");
                }else{
                    sFile = new File(basePath + filePath, filePrefix + ".m4a");
                }
                FileUtils.copyInputStreamToFile(songFile.getInputStream(), sFile);
                List<File> fileList = new ArrayList<File>();
                fileList.add(mFile);
                fileList.add(sFile);
                String fileName = filePrefix+".zip";
                ZipUtils.createZip(fileName,basePath + filePath,fileList,filePrefix);
                song.setPackageUrl(filePath+fileName);
                File file = new File(basePath+filePath+fileName);
                song.setPackageSize(file.length());
            }
            Map<String,Object> params = new HashMap<String, Object>();
            params.put("singerId",songObject.getSingerId());
            params.put("songId",songObject.getId());
            SongSinger songSinger = songService.selectSongSinger(params);
            if(songSinger != null){
                SongSinger ss = new SongSinger();
                ss.setSingerId(Long.valueOf(song.getSingerId()));
                ss.setSongId(song.getId());
                ss.setId(songSinger.getId());
                songService.updateSongSinger(ss);
            }
            int ret = songService.updateByPrimaryKey(song);
            if (ret > 0) {
                resultData.setResultCode("success");
                resultData.setResultMessage("更新成功");
            } else {
                resultData.setResultCode("fail");
                resultData.setResultMessage("更新失败");
            }
        } else {
            resultData.setResultCode("fail");
            resultData.setResultMessage("数据不存在");
        }
        return resultData;
    }

    /**
     * 上传file
     *
     * @param file
     */
    private String uploadFile(@RequestParam MultipartFile file, String filePath, String filePathName) {
        if (file.isEmpty()) {
            return null;
        }
        String fileName = file.getOriginalFilename();
        String sufix = fileName.substring(fileName.lastIndexOf("."));
        String zipPath = File.separator + filePath + File.separator + filePathName + File.separator + DateFormatUtils.format(new Date(), "yyyyMMdd") + File.separator;
        String zipFilename = DateFormatUtils.format(new Date(), "HHmmss") + sufix;
        String zipUrl = "";
        File mFile = null;
        try {
            mFile = new File(basePath + zipPath, zipFilename);
            FileUtils.copyInputStreamToFile(file.getInputStream(), mFile);
            zipUrl = zipPath + zipFilename;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return zipUrl + "," + mFile.length();

    }

}
